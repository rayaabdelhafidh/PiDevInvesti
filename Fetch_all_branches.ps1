# Fetch all branches
Write-Output "Fetching all branches..."
git fetch --all

# Switch to main branch
Write-Output "Switching to main branch..."
git checkout main

# Get all remote branches (excluding main)
$branches = git branch -r | Where-Object { $_ -notmatch 'origin/main' } | ForEach-Object { ($_ -replace 'origin/', '').Trim() }

# Debug: Print the list of branches
Write-Output "List of branches to merge:"
$branches | ForEach-Object { Write-Output $_ }

# Merge each branch into main
foreach ($branch in $branches) {
    Write-Output "Merging $branch into main..."
    $mergeResult = git merge origin/$branch --no-edit --allow-unrelated-histories 2>&1

    if ($LASTEXITCODE -ne 0) {
        Write-Output "Conflict detected while merging $branch. Resolve conflicts and continue."
        Write-Output $mergeResult

        # Pause the script until the user resolves the conflict
        Read-Host "Press Enter after resolving conflicts..."

        # After resolving conflicts, complete the merge
        git add .
        git commit --no-edit
    }
}

# Push the updated main branch
Write-Output "Pushing updates to main branch..."
git push origin main