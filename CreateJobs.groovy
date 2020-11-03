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
  'openpaas-front'
]

repositories.each {
  def repo = it

  multibranchPipelineJob(repo) {
    branchSources {
      github {
        id(repo)
        scanCredentialsId('github')
        repoOwner('OpenPaaS-Suite')
        repository(repo)
        buildOriginPRHead(true)
        buildOriginBranchWithPR(false)
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
