#!/bin/bash

git add --all .
read -p "Commit message: " desc
git commit -m "$desc"
git push origin master 
