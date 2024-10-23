#!/usr/bin/env bash

SCRIPT_DIR=$( cd -- "$( dirname -- "${BASH_SOURCE[0]}" )" &> /dev/null && pwd )

# Remember to put the full path to local archive YAML in -v
docker run -p 8000:8000 -v $SCRIPT_DIR/api/mock-api.yaml:/api.yaml danielgtaylor/apisprout /api.yaml
