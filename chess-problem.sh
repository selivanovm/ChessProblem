#!/bin/sh

params="$@"
./gradlew -q run -Pargs="$params"
