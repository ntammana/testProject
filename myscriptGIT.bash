#!/bin/bash

git add --all .
read -p "Commit message: " desc
git commit -m "$desc"
git push origin master

cd ./testProject
for f in *
do
	cp -v -r $f ../testProjectSVN/nttestproject
done

#bash script to add commit and push files into svn directory
cd ~/testProjectSVN/nttestproject
svn update
svn add * --force

svn commit -m "$desc" 
