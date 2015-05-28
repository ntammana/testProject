#!/bin/bash

#bash script to add commit and push files into svn directory
cd ~/testProject/
svn update
svn add * --force
svn commit -m "automatic SVN push using script"

