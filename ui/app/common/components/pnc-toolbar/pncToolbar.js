/*
 * JBoss, Home of Professional Open Source.
 * Copyright 2014-2019 Red Hat, Inc., and individual contributors
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
  
    /**
     * The component representing toolbar (see https://patternfly.github.io/angular-patternfly/#!/api/patternfly.toolbars.directive:pfToolbar)
     * compatible with pnc-client library.
     */
    angular.module('pnc.common.components').component('pncToolbar', {
      bindings: {
        /**
         * Object: The page representing filtering page (see filteringPaginator factory) coming from pnc-client
         */
        filteringPage: '<',
        /**
         * Array: The fields representing individual select box filtering options, this configuration will extend PatternFly Toolbar configuration
         */
        filteringFields: '<'
      },
      templateUrl: 'common/components/pnc-toolbar/pnc-toolbar.html',
      controller: ['pfFilterAdaptor', Controller]
    });
  
    function Controller(pfFilterAdaptor) {
      var $ctrl = this;

      // -- Controller API --
      $ctrl.pfToolbarConfig = null;

      // --------------------

      $ctrl.$onInit = () => {
        $ctrl.adaptor = pfFilterAdaptor($ctrl.filteringPage);
    
        $ctrl.pfToolbarConfig = {
          isTableView: true,
          filterConfig: {
            fields: $ctrl.filteringFields,
            showTotalCountResults: false,
            appliedFilters: [],
            onFilterChange: $ctrl.adaptor.onFilterChange
          }
        };
      };

    }

  })();
