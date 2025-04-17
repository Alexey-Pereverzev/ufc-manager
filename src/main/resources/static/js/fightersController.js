angular.module('ufcManager', ['ngStorage'])
  .controller('fightersController', function($scope, $localStorage, $http) {

    // ========== Authorization ==========
    // if ($localStorage.ufcUser && $localStorage.ufcUser.token) {
    //   $http.defaults.headers.common.Authorization = 'Bearer ' + $localStorage.ufcUser.token;
    //   $scope.username = $localStorage.ufcUser.username;
    //   $scope.userRole = $localStorage.ufcUser.role;
    // } else {
    //   window.location.href = 'access-denied.html';
    //   return;
    // }

    /* ---------- текущий пользователь (демо) ---------- */
    $scope.username  = 'test_manager';      // пустая строка, если не вошёл
    $scope.userRole  = 'MANAGER';           // GUEST | MANAGER | ADMIN

    $scope.mode = 'list';

    /* --------- утилиты для header --------- */
    $scope.isGuest   = () => $scope.userRole === 'GUEST'  || !$scope.userRole;
    $scope.isManager = () => $scope.userRole === 'MANAGER';
    $scope.isAdmin   = () => $scope.userRole === 'ADMIN';

    $scope.$storage = $localStorage;          // <- сервис доступен в шаблоне
    $scope.fighter  = {};

    $scope.fighters = [     //  MOCK initialization
      {
        id: 1,
        name: 'Jon Jones',
        weightClass: 'Heavyweight',
        country: 'USA',
        ufcRecord: '20-1',
        mmaRecord: '27-1',
        status: 'Active'
      },
      {
        id: 2,
        name: 'Islam Makhachev',
        weightClass: 'Lightweight',
        country: 'Russia',
        ufcRecord: '14-1',
        mmaRecord: '24-1',
        status: 'Active'
      },
      {
        id: 3,
        name: 'Charles Oliveira',
        weightClass: 'Lightweight',
        country: 'Brazil',
        ufcRecord: '21-9',
        mmaRecord: '34-9',
        status: 'Released'
      }
    ];

    $scope.selectedWeightClass = 'All';
    $scope.onlyActive = 'true';
    $scope.weightClasses = [...new Set($scope.fighters.map(f=>f.weightClass))];


    function regroup() {
          let arr = $scope.fighters.slice();

          if ($scope.onlyActive === 'true')  arr = arr.filter(f => f.status === 'Active');
          if ($scope.onlyActive === 'false') arr = arr.filter(f => f.status === 'Released');
          if ($scope.selectedWeightClass !== 'All')
            arr = arr.filter(f => f.weightClass === $scope.selectedWeightClass);

          const grouped = {};
          arr.forEach(f => (grouped[f.weightClass] ||= []).push(f));
          $scope.filtered = grouped;                // <── HTML response
    }
    regroup();                                      // initial view
    $scope.$watchGroup(['selectedWeightClass','onlyActive','fighters'], regroup, {deep:true});


    $scope.logout = () => {                 // просто демо‑действие
        $scope.username = ''; $scope.userRole = '';
    };

    $scope.addFighter = () => {
        $scope.fighter = {status:'Active'};
        $scope.mode    = 'edit';
    };

    $scope.editFighter = f => {
        $scope.fighter = angular.copy(f);
        $scope.mode    = 'edit';
    };


    $scope.saveFighter = () => {
        if ($scope.fighter.id) {                        // update
          const idx = $scope.fighters.findIndex(x=>x.id===$scope.fighter.id);
          if (idx!==-1) $scope.fighters[idx] = angular.copy($scope.fighter);
        } else {                                        // create
          $scope.fighter.id = Date.now();               // простая генерация id
          $scope.fighters.push(angular.copy($scope.fighter));
          $scope.weightClasses = [...new Set($scope.fighters.map(f=>f.weightClass))];
        }
        $scope.cancelEdit();
    };


    $scope.deleteFighter = function () {
      if (confirm('Delete ' + $scope.fighter.name + '?')) {
        $scope.fighters = $scope.fighters.filter(f => f.id !== $scope.fighter.id);
        $scope.cancelEdit();
      }
    };

    $scope.cancelEdit = function () {
      $scope.fighter = {};
      $scope.mode    = 'list';
    };







  });


