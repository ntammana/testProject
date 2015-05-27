#!/bin/bash

#bash script to add, commit and push files in a directory
git add .
read -p "Commit message:" desc
git commit -m "$desc"
git push origin master
