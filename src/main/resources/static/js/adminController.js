angular.module('ufcManager', [])
  .controller('adminController', function($scope, $http) {

        $scope.users = [
          { id: 1, username: 'john_doe', role: 'GUEST', status: 'Active' },
          { id: 2, username: 'anna_admin', role: 'ADMIN', status: 'Active' },
          { id: 3, username: 'mike_manager', role: 'MANAGER', status: 'Blocked' }
        ];

        $scope.roleFilter = '';
        $scope.usernameFilter = '';

        $scope.selectedUser = null;

        $scope.filteredUsers = function() {
          return $scope.users.filter(u => {
            const roleMatch = !$scope.roleFilter || u.role === $scope.roleFilter;
            const nameMatch = !$scope.usernameFilter || u.username.toLowerCase().includes($scope.usernameFilter.toLowerCase());
            return roleMatch && nameMatch;
          });
        };

        $scope.setEditUser = function(user) {
          $scope.selectedUser = angular.copy(user);
        };

        $scope.setDeleteUser = function(user) {
          $scope.selectedUser = user;
        };

        $scope.saveUserChanges = function() {
          const idx = $scope.users.findIndex(u => u.id === $scope.selectedUser.id);
          if (idx !== -1) {
            $scope.users[idx].role = $scope.selectedUser.role;
          }
          bootstrap.Modal.getOrCreateInstance(document.getElementById('editUserModal')).hide();
        };

        $scope.deleteUserConfirmed = function() {
          const idx = $scope.users.findIndex(u => u.id === $scope.selectedUser.id);
          if (idx !== -1) {
            $scope.users[idx].status = 'Blocked';
          }
          bootstrap.Modal.getOrCreateInstance(document.getElementById('deleteUserModal')).hide();
        };

        $scope.restoreUser = function(user) {
          user.status = 'Active';
        };

        $scope.logout = function() {
          $scope.username = '';
          $scope.role = '';
        };

});
