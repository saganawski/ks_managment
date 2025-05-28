FROM eclipse-temurin:8-jdk

# Optional: Maven for global mvn commands
RUN apt-get update && \
    apt-get install -y maven && \
    apt-get clean && rm -rf /var/lib/apt/lists/*

ARG USERNAME=dev
RUN useradd -ms /bin/bash $USERNAME
USER $USERNAME
WORKDIR /workspace
