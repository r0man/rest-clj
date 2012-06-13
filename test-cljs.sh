#!/usr/bin/env bash
if [ ! -f target/rest-test.js ]; then
    lein cljsbuild once
fi
echo "rest.test.run()" | d8 --shell target/rest-test.js
