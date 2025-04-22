angular.module('ufcManager')
  .controller('adminController', function($scope, $http, authService) {

    authService.initUser($scope);

    $scope.logout = function () {
      authService.clearUser($scope);
      window.location.href = 'login.html';
    };

    $scope.getUserById = function(id) {
      return $scope.users.find(u => u.id === id);
    };


    $scope.isUnauthorized = () => authService.isUnauthorized();
    $scope.isGuest        = () => $scope.role === 'GUEST';
    $scope.isManager      = () => $scope.role === 'MANAGER';
    $scope.isAdmin        = () => $scope.role === 'ADMIN';




//     Loading users from the server
     $http.get('/api/users').then(response => {
       $scope.users = response.data;
     });

    $scope.selectedUser = {};
    $scope.roleFilter = '';
    $scope.usernameFilter = '';

    $scope.setEditUser = function(user) {
      $scope.selectedUser = angular.copy(user);
    };

    $scope.setDeleteUser = function(user) {
      $scope.selectedUser = angular.copy(user);
    };

    $scope.saveUserChanges = function() {
      const user = $scope.getUserById($scope.selectedUser.id);
      if (user) {
        user.role = $scope.selectedUser.role;
        $http.put(`/api/users/${user.id}/role`, { role: user.role })
          .then(response => console.log('Role updated successfully'))
          .catch(error => console.error('Role update failed:', error));
      }
    };

    $scope.deleteUserConfirmed = function() {
      const user = $scope.getUserById($scope.selectedUser.id);
      if (user) {
        user.status = 'BLOCKED';
        $http.put(`/api/users/${user.id}/status`, { status: 'BLOCKED' })
          .then(response => console.log('User blocked successfully'))
          .catch(error => console.error('Blocking failed:', error));
      }
    };

    $scope.restoreUser = function(user) {
      const target = $scope.getUserById(user.id);
      if (target) {
        target.status = 'ACTIVE';
        $http.put(`/api/users/${user.id}/status`, { status: 'ACTIVE' })
          .then(response => console.log('User restored'))
          .catch(error => console.error('Restore failed:', error));
      }
    };

    // filter users by role and name
    $scope.filteredUsers = function() {
      return $scope.users.filter(u => {
        const roleMatch = !$scope.roleFilter || u.role === $scope.roleFilter;
        const nameMatch = !$scope.usernameFilter || u.username.toLowerCase().includes($scope.usernameFilter.toLowerCase());
        return roleMatch && nameMatch;
      });
    };

  });


//    $scope.username = 'admin'; // текущий пользователь
//    $scope.role = 'ADMIN';     // роль текущего пользователя
//
//
//    // Моки пользователей
//    $scope.users = [
//      { id: 1, username: 'john_doe', role: 'GUEST', status: 'Active' },
//      { id: 2, username: 'anna_admin', role: 'ADMIN', status: 'Active' },
//      { id: 3, username: 'mike_manager', role: 'MANAGER', status: 'Blocked' }
//    ];