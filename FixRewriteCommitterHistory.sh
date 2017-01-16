#!/usr/bin/sh

git filter-branch --commit-filter '
        if [ "$GIT_COMMITTER_NAME" = "micqdig" ];
        then
                GIT_COMMITTER_NAME="fsmicdev";
                GIT_AUTHOR_NAME="MicG";
                GIT_COMMITTER_EMAIL="fs.micdev@gmail.com";
                GIT_AUTHOR_EMAIL="fs.micdev@gmail.com";
                git commit-tree "$@";
        else
                git commit-tree "$@";
        fi' HEAD
		