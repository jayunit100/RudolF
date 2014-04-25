git filter-branch --index-filter 'git update-index --remove py_tld/sample1.csv' <introduction-revision-sha1>..HEAD
git push --force --verbose --dry-run
git push --force
