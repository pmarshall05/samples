FROM python:3

#============================================
# Required Libraries
#============================================
RUN apt-get -qqy update && \
    apt-get -qqy --no-install-recommends install \
    gconf-service \
    libasound2 \
    libatk1.0-0 \
    libc6 \
    libcairo2 \
    libcups2 \
    libdbus-1-3 \
    libexpat1 \
    libfontconfig1 \
    libgcc1 \
    libgconf-2-4 \
    libgdk-pixbuf2.0-0 \
    libglib2.0-0 \
    libgtk-3-0 \
    libnspr4 \
    libpango-1.0-0 \
    libpangocairo-1.0-0 \
    libstdc++6 \
    libx11-6 \
    libx11-xcb1 \
    libxcb1 \
    libxcomposite1 \
    libxcursor1 \
    libxdamage1 \
    libxext6 \
    libxfixes3 \
    libxi6 \
    libxrandr2 \
    libxrender1 \
    libxss1 \
    libxtst6 \
    fonts-liberation \
    libnss3 \
    lsb-release \
    xdg-utils \
    wget \
    libu2f-udev \
    libvulkan1  \
    openjdk-17-jdk \
    ca-certificates \
    tzdata \
    zip \
    unzip \
    curl \
    wget \
    libqt5webkit5 \
    libgconf-2-4 \
    xvfb \
    gnupg


#===================================
# Node
#===================================
RUN apt-get install -y nodejs
RUN apt-get install -y npm


#===================================
# Google Chrome 
#===================================
RUN apt-get install -y chromium
RUN apt-get install -y chromium-driver


#===================================
# Gecko Driver
#===================================
ENV GECKO_DRIVER_VERSION="v0.33.0"
RUN wget https://github.com/mozilla/geckodriver/releases/download/${GECKO_DRIVER_VERSION}/geckodriver-${GECKO_DRIVER_VERSION}-linux64.tar.gz
RUN tar -xvzf geckodriver*
RUN chmod +x geckodriver
RUN mv geckodriver /usr/local/bin/


#===================================
# Firefox
#===================================
RUN echo "deb http://deb.debian.org/debian/ unstable main non-free contrib" > /etc/apt/sources.list.d/sid.list
RUN apt-get -qqy update
RUN apt-get install -y firefox


#===================================
# Appium
#===================================
RUN npm i --location=global appium
RUN appium driver install uiautomator2


#===============
# Set JAVA_HOME
#===============
ENV JAVA_HOME="/usr/lib/jvm/java-17-openjdk-arm64" PATH=$PATH:$JAVA_HOME/bin


#=====================
# Install Android SDK
#=====================
ARG SDK_VERSION=commandlinetools-linux-10406996_latest
ARG ANDROID_BUILD_TOOLS_VERSION=34.0.0
ARG ANDROID_PLATFORM_VERSION="android-33"

ENV SDK_VERSION=$SDK_VERSION \
    ANDROID_BUILD_TOOLS_VERSION=$ANDROID_BUILD_TOOLS_VERSION \
    ANDROID_HOME=/root/android-sdk

RUN wget -O tools.zip https://dl.google.com/android/repository/${SDK_VERSION}.zip && \
    unzip tools.zip -d /tmp && rm tools.zip && \
    mkdir -p /root/android-sdk/cmdline-tools && \
    mv /tmp/cmdline-tools /root/android-sdk/cmdline-tools/latest && \
    chmod a+x -R $ANDROID_HOME && \
    chown -R root:root $ANDROID_HOME

ENV PATH=$PATH:$ANDROID_HOME/tools:$ANDROID_HOME/tools/bin

# https://askubuntu.com/questions/885658/android-sdk-repositories-cfg-could-not-be-loaded
RUN mkdir -p ~/.android && touch ~/.android/repositories.cfg
RUN echo y | /root/android-sdk/cmdline-tools/latest/bin/sdkmanager "platform-tools" && \
    echo y | /root/android-sdk/cmdline-tools/latest/bin/sdkmanager "build-tools;$ANDROID_BUILD_TOOLS_VERSION" && \
    echo y | /root/android-sdk/cmdline-tools/latest/bin/sdkmanager "platforms;$ANDROID_PLATFORM_VERSION"

ENV PATH=$PATH:$ANDROID_HOME/platform-tools:$ANDROID_HOME/build-tools



#============================================
# Robot Framework Dependencies
#============================================
RUN python3 -m pip install robotframework==5.0.1
RUN python3 -m pip install robotframework-requests==0.9.3
RUN python3 -m pip install robotframework-saucelabs==0.2.1
RUN python3 -m pip install robotframework-selenium2library==3.0.0
RUN python3 -m pip install robotframework-seleniumlibrary==6.0.0
RUN python3 -m pip install robotframework-faker==5.0.0
RUN python3 -m pip install robotframework-axelibrary==0.1.5
RUN python3 -m pip install robotframework-appiumlibrary==2.0.0
RUN python3 -m pip install saucebindings==1.3.0
RUN python3 -m pip install simplejson==3.18.0
RUN python3 -m pip install sa11y==0.2.3
RUN python3 -m pip install selenium==4.9.0