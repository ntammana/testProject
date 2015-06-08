#!/bin/bash
#CONFIG_FILE=~/bashconfig.conf
#if [[ -f $CONFIG_FILE ]]; then
#	. $CONFIG_FILE
#fi

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
touch differences.txt
diff -qr ~/testProject/src ~/nttestproject/src >differences.txt
rm -rf ~/nttestproject/src
#cd ~/testProject/src
mkdir ~/nttestproject/src
for f in *
do
	cp -r $f ~/nttestproject/src
done

#bash script to add commit and push files into svn directory
read -p "Ready to commit and push into svn directory? (y/n)" svnanswer
if [ $svnanswer = y ]
touch difference.txt
then
{
	cd ~/nttestproject/src
#	svn update
	svn add * --force
	svn rm -qr diff ./testProject/src ./nttestproject/src
	svn commit -m "$desc" 
}
else
{
	exit
}
fi
