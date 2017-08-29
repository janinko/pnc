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

  angular.module('pnc.build-configs').component('pncCreateBuildConfigWizard', {
    templateUrl: 'build-configs/directives/pnc-create-build-config-wizard/pnc-create-build-config-wizard.html',
    controller: ['$log', '$timeout', Controller],
    bindings: {
      project: '<',
      onClose: '&close'
    }
  });

  function Controller($log, $timeout) {
    var $ctrl = this;

    // -- Controller API --

    $ctrl.false = false;
    $ctrl.wizardData = {};
    $ctrl.reviewPageShown = false;
    $ctrl.updateDependencies = updateDependencies;
    $ctrl.onShowReviewSummary = onShowReviewSummary;

    // --------------------

    $ctrl.$onInit = function () {
      $log.debug('pnc-create-build-config-wizard::$onInit');
    };

    function updateDependencies(buildConfigs) {
      $ctrl.buildConfig.dependencies = buildConfigs.map(function (bc) { return bc.id; });
    }

    function onShowReviewSummary() {
      $ctrl.reviewPageShown = true;
      $timeout(function () {
        $ctrl.reviewPageShown = false;  // done so the next time the page is shown it updates
      });
    }
  }
})();
