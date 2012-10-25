#! /bin/sh -e

git checkout master
lein doc!
git checkout gh-pages
rsync -r doc/ .
git add .
git commit
git push -u origin gh-pages
git checkout master
