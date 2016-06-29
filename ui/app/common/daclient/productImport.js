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

  var module = angular.module('pnc.common.daclient');

  module.config([
    'jsonrpcProvider',
    function(jsonrpcProvider) {
      jsonrpcProvider.interceptors.push(function(){
        return {
          requestStarted: function(requestFinishedPromise) {
            console.log('socket request started');

            requestFinishedPromise.finally(function(){
              console.log('socket request finished');
            });
          }
        }
      })
    }
  ]);

  module.factory('productImport', [
    '$log',
    'daConfig',
    'jsonrpc',
    function ($log, daConfig, jsonrpc) {

      var rpc = jsonrpc.wsClient(daConfig.getDaImportRpcUrl());
      var productImport = {};

      /**
       *
       */
      productImport.start = function (params) {
        $log.debug('productImport.start() %O', params);
        return rpc.invoke('buildConfiguration.product.start', params);
      };

      /**
       *
       */
      productImport.nextLevel = function (params) {
        $log.debug('productImport.nextLevel() %O', params);
        return rpc.invoke('buildConfiguration.product.nextLevel', params);
      };

      /**
       *
       */
      productImport.finish = function (params) {
        $log.debug('productImport.finish() %O', params);
        return rpc.invoke('buildConfiguration.product.finish', params);
      };

      return productImport;
    }
  ]);

})();
