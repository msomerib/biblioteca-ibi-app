FROM mcr.microsoft.com/devcontainers/base:ubuntu

# --------------------------------------------------------
# Pacotes básicos
# --------------------------------------------------------
RUN apt-get update && apt-get install -y --no-install-recommends \
    wget unzip curl git ca-certificates \
    lib32stdc++6 lib32z1 \
    && rm -rf /var/lib/apt/lists/*

# --------------------------------------------------------
# Instalar JDK 17
# --------------------------------------------------------
RUN apt-get update && apt-get install -y openjdk-17-jdk

ENV JAVA_HOME=/usr/lib/jvm/java-17-openjdk-amd64
ENV PATH="${JAVA_HOME}/bin:${PATH}"

# --------------------------------------------------------
# Instalar Android SDK
# --------------------------------------------------------
ENV ANDROID_HOME=/usr/local/android-sdk
ENV ANDROID_SDK_ROOT=/usr/local/android-sdk
ENV PATH="${PATH}:${ANDROID_HOME}/cmdline-tools/latest/bin:${ANDROID_HOME}/platform-tools:${ANDROID_HOME}/emulator"

RUN mkdir -p $ANDROID_HOME/cmdline-tools

# Baixar Command-line Tools
RUN cd /tmp && \
    wget https://dl.google.com/android/repository/commandlinetools-linux-11076708_latest.zip -O tools.zip && \
    unzip tools.zip -d /tmp/tools && \
    mv /tmp/tools/cmdline-tools $ANDROID_HOME/cmdline-tools/latest && \
    rm -rf /tmp/tools.zip

# Aceitar licenças automaticamente
RUN yes | ${ANDROID_HOME}/cmdline-tools/latest/bin/sdkmanager --licenses

# --------------------------------------------------------
# Instalar platform-tools, build-tools e SDK 35
# --------------------------------------------------------
RUN yes | sdkmanager --install \
    "platform-tools" \
    "platforms;android-35" \
    "build-tools;35.0.0" \
    "cmdline-tools;latest"

# --------------------------------------------------------
# Instalar Gradle
# --------------------------------------------------------
RUN wget https://services.gradle.org/distributions/gradle-8.7-bin.zip -P /tmp \
    && unzip /tmp/gradle-8.7-bin.zip -d /opt \
    && rm /tmp/gradle-8.7-bin.zip

ENV GRADLE_HOME=/opt/gradle-8.7
ENV PATH="${PATH}:${GRADLE_HOME}/bin"

# --------------------------------------------------------
# Ajustes finais
# --------------------------------------------------------
RUN mkdir -p /workspace
WORKDIR /workspace
