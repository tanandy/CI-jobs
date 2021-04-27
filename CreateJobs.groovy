def repositories = [
  'php-deps-composer',
  'esn-api-client',
  'esn-dav-import-client',
  'esn-frontend-account-inbox',
  'esn-frontend-account',
  'esn-frontend-admin',
  'esn-frontend-calendar',
  'esn-frontend-calendar-public',
  'esn-frontend-common-libs',
  'esn-frontend-contacts',
  'esn-frontend-group',
  'esn-frontend-inbox',
  'esn-frontend-inbox-calendar',
  'esn-frontend-inbox-james',
  'esn-frontend-inbox-linshare',
  'esn-frontend-linshare',
  'esn-frontend-login',
  'esn-frontend-mailto',
  'esn-frontend-mailto-handler',
  'esn-frontend-videoconference-calendar',
  'esn-james-api-client',
  'jmap-client-ts',
  'openpaas-front'
]

repositories.each {
  def repo = it

  multibranchPipelineJob(repo) {
    branchSources {
        branchSource {
            source {
                github {
                    id(repo)
                    credentialsId('github')
                    repoOwner('OpenPaaS-Suite')
                    repository(repo)
                    repositoryUrl('')
                    configuredByUrl(false)
                    traits {
                        gitHubTagDiscovery()
                        gitHubBranchDiscovery { strategyId(1) }
                        gitHubPullRequestDiscovery { strategyId(1) }
                    }
                }
            }
            buildStrategies {
                buildAnyBranches {
                    buildChangeRequests {
                        ignoreTargetOnlyChanges(true)
                        ignoreUntrustedChanges(false)
                    }
                    buildNamedBranches {
                        filters {
                            exact {
                                name('master')
                                caseSensitive(true)
                            }
                        }
                    }
                    buildTags {
                        atLeastDays '-1'
                        atMostDays '14'
                    }
                }
            }
        }
    }
    configure {
        // workaround for JENKINS-46202 (https://issues.jenkins-ci.org/browse/JENKINS-46202)
        def traits = it / sources / data / 'jenkins.branch.BranchSource' / source / traits
        traits << 'org.jenkinsci.plugins.github_branch_source.ForkPullRequestDiscoveryTrait' {
            strategyId 1
            trust(class: 'org.jenkinsci.plugins.github_branch_source.ForkPullRequestDiscoveryTrait$TrustPermission')
        }
    }
    triggers {
      periodicFolderTrigger {
        interval('1')
      }
    }
    orphanedItemStrategy {
        discardOldItems {
            numToKeep(20)
        }
    }
  }
}

listView('OpenPaas-Suite') {
  jobs {
    names(repositories.toArray(new String[repositories.size()]))
  }
  columns {
    status()
    weather()
    name()
    lastSuccess()
    lastFailure()
    lastDuration()
    buildButton()
  }
}
