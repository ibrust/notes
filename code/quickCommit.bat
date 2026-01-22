:: Effectively this script is squashing a new commit into your previous commmit, retaining the previous commit message, and pushing the change to github.
:: Make sure your PR's initial commit already exists before using this script, otherwise it'll just squash your work into the previous commit of a different PR.
:: steps of this process are:
:: 1) adds your unstaged changes 
:: 2) the soft reset command moves back a commit, but moves the previous commits changes into the staging area (called the "working directory"). 
:: So now both your new + previous changes are staged
:: 3) commits the changes while retaining the previous commit message.
:: 4) `push.autoSetupRemote true` automatically associates the local branch with an upstream branch of the same name.
:: 5) `push.default simple` configures git to push the branch you're currently on if you don't specify the branch name
:: 6) force push the new squashed commit to the matching branch on github (so only run this script if you're sure you want to keep what you're committing)
:: note that the script setupShell.bat (run during linting) also sets up an alias qc for quickCommit.bat
git add .
git add ../
git reset --soft HEAD~1
git commit -C @{1}
git config --global push.autoSetupRemote true
git config --global push.default simple
git push -f