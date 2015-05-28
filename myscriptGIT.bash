#!/bin/bash

git add --all .
read -p "Commit message: " desc
git commit -m "$desc"
git push origin master

cd ./testProject
for f in *
do
	cp -v -r $f ../testProjectSVNauto/
done
 
