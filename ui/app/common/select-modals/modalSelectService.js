/*
 * JBoss, Home of Professional Open Source.
 * Copyright 2014 Red Hat, Inc., and individual contributors
 * as indicated by the @author tags.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
(function () {
  'use strict';

  angular.module('pnc.common.select-modals').service('modalSelectService', [
    '$modal',
    '$q',
    '$rootScope',
    function ($modal, $q, $rootScope) {

      /**
       * Opens a modal window for multiple selection of Build Groups (aka BuildConfigurationSets).
       * Takes a config object that can have the following properties:
       *
       *  title {String} - The title to display in the modal window
       *  selected {Array} - An array of BuildGroups that are initially selected.
       */
      this.openForBuildGroup = function (config) {
        var modal = $modal.open({
          animation: true,
          size: 'lg',
          templateUrl: 'common/select-modals/build-group-multi-select.html',
          controller: 'BuildGroupMultiSelectController',
          controllerAs: 'ctrl',
          bindToController: true,
          resolve: {
            modalConfig: function () {
              return $q.when(config);
            }
          },
        });

        modal.result.then(function () {
          $rootScope.$evalAsync();
        });

        return modal;
      };
    }
  ]);

})();
