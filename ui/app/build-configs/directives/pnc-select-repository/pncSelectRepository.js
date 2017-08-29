(function () {
  'use strict';

  angular.module('pnc.build-configs').component('pncSelectRepository', {
    require: {
      ngModel: 'ngModel'
    },
    templateUrl: 'build-configs/directives/pnc-select-repository/pnc-select-repository.html',
    controller: ['$log', '$q', 'utils', 'pncNotify', 'RepositoryConfiguration', 'pncProperties', Controller]
  });

  function Controller($log, $q, utils, pncNotify, RepositoryConfiguration, pncProperties) {
    var $ctrl = this,
        loading = false,
        previousDigest;


    // -- Controller API --
    
    $ctrl.userData = {};
    $ctrl.checkForRepo = checkForRepo;
    $ctrl.isLoading = isLoading;
    $ctrl.isRepoInternal = isRepoInternal;

    // --------------------


    $ctrl.$onInit = function () {
      $ctrl.ngModel.$render = function () {
        $ctrl.userData = $ctrl.ngModel.$viewValue;
      };
    };

    $ctrl.$doCheck = function () {
      var latestDigest = digest();
      if (previousDigest !== latestDigest) {
        $ctrl.ngModel.$setViewValue(parseViewData());
        previousDigest = latestDigest;
      }
    };

    function checkForRepo(url) {
      $ctrl.userData.selectedRepoConfig = undefined;
      $ctrl.multipleRCError = false;
      loading = true;

      getRepo(url).then(function (repo) {
        if (repo && repo.id) {
          $ctrl.userData.selectedRepoConfig = repo;
        }

      }).finally(function() { loading = false; });
    }

    function getRepo(url) {
      if (utils.isEmpty(url)) {
        return $q.when();
      }
      return RepositoryConfiguration.match({ search: url }).$promise.then(function (result) {
        var repos = result.data;

        if (repos.length === 1) {
          return repos[0];
        } else if (repos.length > 1) {
          $ctrl.multipleRCError = true;
          return undefined;
        } else {
          // No repo config exists
          return undefined;
        }
      });
    }

    function isLoading() {
      return loading;
    }

    function isRepoInternal(url) {
      if (url) {
        return url.includes(pncProperties.internalScmAuthority);
      }
    }

    function parseViewData() {
      var parsed = {
        scmUrl: $ctrl.userData.scmUrl,
        revision: $ctrl.userData.revision,
        preBuildSyncEnabled: $ctrl.userData.preBuildSyncEnabled,
        useExistingRepoConfig: angular.isDefined($ctrl.userData.selectedRepoConfig)
      };

      if (angular.isDefined($ctrl.userData.selectedRepoConfig)) {
        parsed.repoConfig = $ctrl.userData.selectedRepoConfig;
      }

      return parsed;
    }

    function digest() {
      var repoId = $ctrl.userData.selectedRepoConfig ? $ctrl.userData.selectedRepoConfig.id : undefined;
      return '' + $ctrl.userData.scmUrl + $ctrl.userData.revision + $ctrl.userData.preBuildSyncEnabled + repoId;
    }

  }

})();
