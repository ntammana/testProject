#!/bin/bash

read -p "Ready to push to git? (y/n)" gitanswer
if [ $gitanswer = y ]
then
{
	git add --all .
	read -p "Commit message: " desc
	git commit -m "$desc"
	git push origin master
}
else 
{
	exit
}
fi
echo "moving project to local svn directory"
#cd ./testProject
rm ../testProjectSVN/nttestproject/*
for f in *
do
	cp -r $f ../testProjectSVN/nttestproject
done

#bash script to add commit and push files into svn directory
read -p "Ready to commit and push into svn directory? (y/n)" svnanswer
if [ $svnanswer = y ]
then
{
	cd ~/testProjectSVN/nttestproject
	svn update
	svn add * --force

	svn commit -m "$desc" 
}
else
{
	exit
}
fi
