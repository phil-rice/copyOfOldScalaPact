#!/bin/bash

function publishDocs {
    NAME=$1
    sbt $NAME/clean $NAME/paradox $NAME/makeSite $NAME/ghpagesPushSite
}

publishDocs "docs"
