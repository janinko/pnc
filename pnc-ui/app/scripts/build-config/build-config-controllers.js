'use strict';

(function() {

  var module = angular.module('pnc.BuildConfig');

  module.controller('BuildConfigCtrl',
    ['$scope', '$state', 'Product', 'Version',
    function($scope, $state, Product, Version) {

      /* Creates new column object for use with the column-browse-column
       * directive the column browse UI element directive. The object will
       * be wired up so as to automatically keep child and parent objects
       * in sync.
       *
       * param: navigateFn - a function which should navigate to the
       *        desired UI state when an item in the column is clicked.
       *        The function will be passed the clicked item as the sole
       *        parameter.
       *
       * param: updateListFn - a function that should return the list of
       *         items to display.
       *
       * param: parent - the column's parent column.
       */
      var newColumn = function(navigateFn, updateListFn, parent) {
        var columnPrototype = {
          parent: null,
          child: null,
          list: [],
          selected: null,

          clearSelected: function() {
            var that = this;
            (function() {
              that.selected = null;
              if (that.child) {
                that.child.clearSelected();
              }
            })();
          }
        };

        var newCol = Object.create(columnPrototype);

        if (parent) {
          newCol.parent = parent;
          parent.child = newCol;
        }

        newCol.click = function(clickedItem) {
          console.log('Click >> clickedItem: %O', clickedItem);
          newCol.selected = clickedItem;
          if (newCol.child) {
            newCol.child.updateList();
          }
          navigateFn(clickedItem);
        };

        newCol.updateList = function() {
          newCol.clearSelected();
          newCol.list = updateListFn();
        };

        return newCol;
      };

      /*
       * Create columns for GUI.
       */

      var productCol = newColumn(
        function(product) {
          $state.go('build-config.product.show', {
            productId: product.id });
        },
        function() {
          return Product.query();
        }
      );

      var versionCol = newColumn(
        function(version) {
          console.log('versionCol >> selected: %O', version);
        },
        function() {
          console.log('versionCol.updateList >> productCol = %O', productCol);
          return Version.query({ productId:
            productCol.selected.id });
        },
        productCol
      );

      // Add columns to scope so can be accessed in the view and
      // from inherriting controllers.
      $scope.columnBrowse = {
        products: productCol,
        versions: versionCol
      }

      // Initialise the first column with values.
      $scope.columnBrowse.products.updateList();
    }
  ]);

  module.controller('ProductListCtrl',
    ['$scope', '$state', '$stateParams', 'productList',
    function($scope, $state, $stateParams, productList) {
      console.log('currentProduct=%O', $scope.currentProduct);
      $scope.products = productList;
      $scope.navigate = function(product) {
        $state.go('build-config.product-list.product',
          { productId: product.id });
      };

      console.log('ProductListCtrl::productList=%O', productList);
    }
  ]);

  module.controller('ProductCtrl', ['$scope', '$stateParams', 'productDetails',
    function ($scope, $stateParams, productDetails) {
      console.log('ProductCtrl::productDetails=%O', productDetails);
      $scope.product = productDetails;
    }
  ]);

  module.controller('VersionListCtrl', ['$scope', '$stateParams', 'versionList',
    function ($scope, $stateParams, versionList) {
      console.log('VersionListCtrl::versionList=%O', versionList);
      console.log('VersionListCtrl::$stateParams=%O', $stateParams);
      $scope.versions = versionList;
    }
  ]);

  module.controller('VersionCtrl',
    ['$scope', '$stateParams', '$state', 'versionDetails',
    function ($scope, $stateParams, $state, versionDetails) {
      console.log('VersionCtrl::versionDetails=%O', versionDetails);
      console.log('VersionCtrl::$stateParams=%O', $stateParams);
      console.log('VersionCtrl::$state=%O', $state);
      console.log('VersionCtrl::$scope=%O', $scope);
      $scope.version = versionDetails;
    }
  ]);

})();
