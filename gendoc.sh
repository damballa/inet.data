#! /bin/sh -e

lein doc
git checkout gh-pages
sleep 2
find tmp/codox -type f -exec touch {} +
rsync -r tmp/codox/ .
