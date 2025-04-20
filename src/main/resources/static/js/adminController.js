angular.module('ufcManager')
  .controller('adminController', function($scope, $http, authService) {

    authService.initUser($scope);

    $scope.logout = function () {
      authService.clearUser($scope);
      window.location.href = 'login.html';
    };


    $scope.username = 'admin'; // текущий пользователь
    $scope.role = 'ADMIN';     // роль текущего пользователя

    $scope.isAdmin = () => $scope.role === 'ADMIN';
    $scope.isManager = () => $scope.role === 'MANAGER';
    $scope.isGuest = () => !$scope.role || $scope.role === 'GUEST';

    // Моки пользователей
    $scope.users = [
      { id: 1, username: 'john_doe', role: 'GUEST', status: 'Active' },
      { id: 2, username: 'anna_admin', role: 'ADMIN', status: 'Active' },
      { id: 3, username: 'mike_manager', role: 'MANAGER', status: 'Blocked' }
    ];

    // --- ЗАГОТОВКИ HTTP ЗАПРОСОВ ---

    // Загрузка пользователей с сервера
    // $http.get('/api/users').then(response => {
    //   $scope.users = response.data;
    // });

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
      const idx = $scope.users.findIndex(u => u.id === $scope.selectedUser.id);
      if (idx !== -1) {
        $scope.users[idx].role = $scope.selectedUser.role;

    // $http.put(/api/users/${user.id}/role, { role: user.role })
            //   .then(response => console.log('Role updated successfully'))
            //   .catch(error => console.error('Role update failed:', error));
      }
    };

    $scope.deleteUserConfirmed = function() {
      const idx = $scope.users.findIndex(u => u.id === $scope.selectedUser.id);
      if (idx !== -1) {
        $scope.users[idx].status = 'Blocked';

        // $http.put(/api/users/${user.id}/status, { status: 'Blocked' })
        //   .then(response => console.log('User blocked successfully'))
        //   .catch(error => console.error('Blocking failed:', error));
      }
    };

    $scope.restoreUser = function(user) {
      const idx = $scope.users.findIndex(u => u.id === user.id);
      if (idx !== -1) {
        $scope.users[idx].status = 'Active';

      // $http.put(/api/users/${user.id}/status, { status: 'Active' })
      //   .then(response => console.log('User restored'))
      //   .catch(error => console.error('Restore failed:', error));
      }
    };

    $scope.logout = () => {
      $scope.username = '';
      $scope.role = '';
    };

    // Опционально: фильтр пользователей по роли и имени
    $scope.filteredUsers = function() {
      return $scope.users.filter(u => {
        const roleMatch = !$scope.roleFilter || u.role === $scope.roleFilter;
        const nameMatch = !$scope.usernameFilter || u.username.toLowerCase().includes($scope.usernameFilter.toLowerCase());
        return roleMatch && nameMatch;
      });
    };

  });
