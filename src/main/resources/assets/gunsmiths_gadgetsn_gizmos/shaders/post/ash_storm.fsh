#version 330

uniform sampler2D InSampler;

layout(std140) uniform AshStormConfig {
    float Progress;
    float Time;
    float SootAmount;
};

in vec2 texCoord;
out vec4 fragColor;

// --- 1. Dave Hoskins 高品質ハッシュ関数（モアレ・周期性の完全排除） ---
vec3 N13(float p) {
    vec3 p3 = fract(vec3(p) * vec3(.1031, .11369, .13787));
    p3 += dot(p3, p3.yzx + 19.19);
    return fract(vec3((p3.x + p3.y) * p3.z, (p3.x + p3.z) * p3.y, (p3.y + p3.z) * p3.x));
}

float N21(vec2 p) {
    vec3 p3 = fract(vec3(p.xyx) * .1031);
    p3 += dot(p3, p3.yzx + 33.33);
    return fract((p3.x + p3.y) * p3.z);
}

float noise(vec2 p) {
    vec2 i = floor(p);
    vec2 f = fract(p);
    f = f * f * (3.0 - 2.0 * f);
    return mix(
    mix(N21(i + vec2(0.0, 0.0)), N21(i + vec2(1.0, 0.0)), f.x),
    mix(N21(i + vec2(0.0, 1.0)), N21(i + vec2(1.0, 1.0)), f.x), f.y
    );
}

float sootFbm(vec2 uv) {
    float n = noise(uv * 4.0) * 0.65;
    n += noise(uv * 9.0) * 0.35;
    return n;
}

// --- 2. 【新機能】熱気・暴風による空間屈折ベクトル（陽炎効果） ---
vec2 getHeatRefraction(vec2 uv, float time, float amount) {
    vec2 windUv = vec2(uv.x * 1.777 + uv.y * 0.5, uv.y - uv.x * 0.5);
    vec2 streakPos = vec2(windUv.x * 16.0, windUv.y * 2.2 + time * 0.12);

    float n = noise(streakPos);
    // 微分勾配による屈折法線ベクトル
    vec2 normal = vec2(
    noise(streakPos + vec2(0.06, 0.0)) - n,
    noise(streakPos + vec2(0.0, 0.06)) - n
    );
    return normal * smoothstep(0.50, 0.95, n) * amount * 0.016;
}

// --- 3. 画面を高速で横切る灰と火の粉の流線 ---
void getFlyingStreaks(vec2 uv, float time, float amount, out vec3 streakColor, out float streakMask) {
    vec2 windUv = vec2(uv.x * 1.777 + uv.y * 0.6, uv.y - uv.x * 0.6);
    vec2 streakPos = vec2(windUv.x * 20.0, windUv.y * 2.8 + time * 0.12);

    float n1 = noise(streakPos);
    float n2 = noise(streakPos * 2.0 + vec2(time * 0.04, 0.0));
    float line = smoothstep(0.74, 0.97, n1 * n2);

    float isFire = step(0.72, N21(floor(streakPos)));
    vec3 col = mix(vec3(0.02, 0.015, 0.02), vec3(1.0, 0.42, 0.08), isFire);

    streakColor = col * line * amount;
    streakMask = line * amount;
}

// --- 4. レンズ付着灰＆煤汚れ（N13ハッシュによる完全不揃い配置） ---
void getAshFlakes(vec2 uv, float time, float amount, out float outFlake, out vec3 outEmber) {
    vec2 aspectUv = vec2(uv.x * 1.777, uv.y);

    // レンズ表面の擦れ汚れ
    vec2 smearUv = aspectUv * 4.5 + vec2(time * 0.002, time * 0.001);
    float smear = smoothstep(0.62, 0.95, sootFbm(smearUv)) * 0.35;

    // 灰の粉塵クラスタ
    vec2 p = aspectUv * 12.0;
    vec2 id = floor(p);
    vec2 gv = fract(p) - vec2(0.5);

    float maxFlake = 0.0;
    vec3 emberAcc = vec3(0.0);

    for (int y = -1; y <= 1; y++) {
        for (int x = -1; x <= 1; x++) {
            vec2 offset = vec2(float(x), float(y));
            vec2 cellId = id + offset;
            float h = N21(cellId);
            vec3 rand3 = N13(h * 142.33);

            if (rand3.x > 0.56) {
                vec2 center = (rand3.yz - vec2(0.5)) * 0.65;
                vec2 dVec = gv - offset - center;

                // 風圧による斜め擦れ
                float windAngle = 0.45;
                vec2 rotatedVec = vec2(
                dVec.x * cos(windAngle) - dVec.y * sin(windAngle),
                (dVec.x * sin(windAngle) + dVec.y * cos(windAngle)) * 1.75
                );

                float shapeNoise = noise(dVec * 12.0 + cellId);
                float dist = length(rotatedVec) + shapeNoise * 0.06;

                float size = mix(0.07, 0.20, rand3.y);
                float lifeCycle = fract(time * 0.011 + rand3.z);
                float life = smoothstep(0.0, 0.15, lifeCycle) * smoothstep(1.0, 0.68, lifeCycle);

                float flake = smoothstep(size, size * 0.12, dist) * life;
                maxFlake = max(maxFlake, flake);

                if (rand3.z > 0.80 && lifeCycle < 0.42) {
                    float emberDist = length(dVec);
                    float emberIntensity = smoothstep(0.045, 0.0, emberDist) * (1.0 - lifeCycle / 0.42);
                    float flicker = 0.8 + 0.2 * sin(time * 0.4 + rand3.x * 40.0);
                    emberAcc += vec3(1.0, 0.42, 0.08) * emberIntensity * life * flicker;
                }
            }
        }
    }

    outFlake = clamp((maxFlake * 0.85 + smear * 0.35) * amount, 0.0, 1.0);
    outEmber = emberAcc * amount;
}

// --- 5. メインパイプライン ---
void main() {
    vec2 uv = texCoord - vec2(0.5);
    float dist = length(uv);
    float vignette = smoothstep(0.20, 0.68, dist);

    // 【1】熱気屈折 ＋ 画面端の色収差サンプリング
    vec2 heatRefraction = getHeatRefraction(texCoord, Time, SootAmount * Progress);
    vec2 aberrationOffset = uv * pow(dist, 1.8) * 0.015 * Progress;
    vec2 sampleUv = texCoord + heatRefraction;

    float r = texture(InSampler, sampleUv - aberrationOffset).r;
    float g = texture(InSampler, sampleUv).g;
    float b = texture(InSampler, sampleUv + aberrationOffset).b;
    vec3 sceneColor = vec3(r, g, b);

    // 【2】彩度低下 ＋ シネマティックトーンマッピング
    float gray = dot(sceneColor, vec3(0.299, 0.587, 0.114));
    vec3 desaturated = mix(sceneColor, vec3(gray), clamp(Progress * 0.50, 0.0, 1.0));

    vec3 contrast = desaturated * desaturated * (3.0 - 2.0 * desaturated);
    vec3 baseRgb = mix(desaturated, contrast, clamp(Progress * 0.35, 0.0, 1.0));

    // 【3】煤煙ビネット ＋ 四隅の赤熱余燼グロー
    vec2 sootCoords = texCoord + vec2(sin(Time * 0.010 + texCoord.y * 3.0) * 0.03, Time * 0.008);
    float cloud = sootFbm(sootCoords);
    float sootMask = smoothstep(0.32, 0.82, vignette * (0.5 + cloud * 0.7));
    float sootIntensity = sootMask * clamp(SootAmount, 0.0, 1.0);

    vec3 emberGlow = vec3(0.40, 0.08, 0.01) * pow(vignette, 2.2) * (0.7 + 0.3 * sin(Time * 0.03 + texCoord.x * 4.0));
    vec3 sootColor = vec3(0.015, 0.01, 0.015) + emberGlow * 1.3;

    float fineGrain = mix(1.0, 0.75 + 0.25 * N21(texCoord * 120.0 + fract(Time)), sootMask);
    baseRgb = mix(baseRgb * fineGrain, sootColor, clamp(sootIntensity * 0.70, 0.0, 0.80));

    // 【4】灰嵐の高速流線
    vec3 streakCol = vec3(0.0);
    float streakMask = 0.0;
    getFlyingStreaks(texCoord, Time, clamp(SootAmount * 1.2, 0.0, 1.0) * Progress, streakCol, streakMask);
    baseRgb = mix(baseRgb, streakCol, clamp(streakMask * 0.65, 0.0, 1.0));

    // 【5】レンズ付着灰・煤汚れ
    float centerMask = smoothstep(0.10, 0.52, dist);
    float flakeAlpha = 0.0;
    vec3 emberColor = vec3(0.0);

    getAshFlakes(texCoord, Time, clamp(SootAmount * 1.3, 0.0, 1.0) * Progress, flakeAlpha, emberColor);
    flakeAlpha *= mix(0.20, 1.0, centerMask);

    vec3 flakeColor = vec3(0.015, 0.012, 0.015) + emberColor * 1.6;
    vec3 finalRgb = mix(baseRgb, flakeColor, clamp(flakeAlpha * 0.80, 0.0, 0.90));

    fragColor = vec4(finalRgb, texture(InSampler, texCoord).a);
}