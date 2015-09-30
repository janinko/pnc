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
'use strict';

(function () {

  var module = angular.module('pnc.record');

  /**
   * @author Jakub Senko
   */
  module.directive('pncProductVersionBCSets', [
    '$log',
    '$state',
    'ProductVersionDAO',
    'BuildConfigurationSetDAO',
    function ($log, $state, ProductVersionDAO, BuildConfigurationSetDAO) {

      return {
        restrict: 'E',
        templateUrl: 'product/directives/pncProductVersionBCSets/pnc-product-version-bcsets.html',
        scope: {
          version: '=',
          product: '='
        },
        link: function (scope) {

          scope.page = ProductVersionDAO.getPagedBCSets({versionId: scope.version.id });

          // Executing a build of a configurationSet forcing all the rebuilds
          scope.forceBuildConfigSet = function(configSet) {
            $log.debug('**Initiating FORCED build of SET: %s**', configSet.name);
            BuildConfigurationSetDAO.forceBuild({
              configurationSetId: configSet.id
            }, {});
          };

          // Executing a build of a configurationSet NOT forcing all the rebuilds
          scope.buildConfigSet = function(configSet) {
            $log.debug('**Initiating build of SET: %s**', configSet.name);
            BuildConfigurationSetDAO.build({
              configurationSetId: configSet.id
            }, {});
          };

        }
      };
    }
  ]);

})();
