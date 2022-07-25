def isReleaseBranch(branchName){

    //Ensure specific Branch is choosen
    branchName == "master" || branchName ==~ /release-2022-[0-9]+/ || branchName == "main"
}
