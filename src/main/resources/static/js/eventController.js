angular.module('ufcManager', [])
  .controller('eventController', function($scope, $http) {

    $scope.mode = 'list';  // list | edit | fightEdit | view

    $scope.username  = 'john_doe';        // или ''
    $scope.role      = 'MANAGER';         // GUEST | MANAGER | ADMIN

    $scope.isAdmin   = () => $scope.role === 'ADMIN';
    $scope.isManager = () => $scope.role === 'MANAGER';
    $scope.isGuest   = () => !$scope.role || $scope.role === 'GUEST';

    $scope.viewTournament = t => {
      $scope.tournament = t;
      $scope.mode = 'view';
    };

    // MOCK tournaments
    $scope.tournaments = [
      {
        id: 1,
        name: 'UFC 300',
        date: '2025-06-30',
        location: 'Las Vegas',
        fights: [
          {
            id: 1,
            fighter1: 'Jon Jones',
            result: 'def.',
            fighter2: 'Stipe Miocic',
            weightClass: 'Heavyweight'
          },
          {
            id: 2,
            fighter1: 'Charles Oliveira',
            result: 'vs.',
            fighter2: 'Islam Makhachev',
            weightClass: 'Lightweight'
          }
        ]
      },
      {
        id: 2,
        name: 'UFC 299',
        date: '2025-06-01',
        location: 'Miami',
        fights: [
          {
            id: 3,
            fighter1: 'Sean O\'Malley',
            result: 'def.',
            fighter2: 'Petr Yan',
            weightClass: 'Bantamweight'
          }
        ]
      }
    ];

    // $http.get('/api/tournaments')
    //   .then(response => {
    //     $scope.tournaments = response.data;
    //   })
    //   .catch(error => {
    //     console.error("Error loading tournaments:", error);
    //   });

    $scope.weightClasses = [
      'Flyweight', 'Bantamweight', 'Featherweight', 'Lightweight',
      'Welterweight', 'Middleweight', 'Light Heavyweight', 'Heavyweight'
    ];

    $scope.resultTypes = ['def.', 'vs.'];

    /* ---------- helpers ---------- */
    const toDateObj = str => new Date(str + 'T00:00:00');          // -> Date (лок.)
    const toYmdStr  = d   => {                                     // -> 'yyyy-MM-dd'
        const pad = n => ('0'+n).slice(-2);
        return `${d.getFullYear()}-${pad(d.getMonth()+1)}-${pad(d.getDate())}`;
    };

    $scope.fight = {};

    // $http.get(`/api/tournaments/${id}`)      GET TOURNAMENT BY ID
    //   .then(response => {
    //     $scope.tournament = response.data;
    //     $scope.mode = 'view';
    //   })
    //   .catch(error => console.error("Error loading tournament:", error));

    $scope.addTournament = () => {
      $scope.tournament = { date: new Date(), fights: [] };      // сразу Date‑obj
      $scope.mode = 'edit';
    };

    $scope.editTournament = t => {
      $scope.tournament = angular.copy(t);
      $scope.tournament.date = toDateObj(t.date);                // ← Date‑obj
      $scope.mode = 'edit';
    };


    $scope.saveTournament = () => {
      // превращаем обратно в строку локальной даты
      $scope.tournament.date = toYmdStr($scope.tournament.date);

      if ($scope.tournament.id) {                       // update
          const i = $scope.tournaments.findIndex(x => x.id === $scope.tournament.id);
          if (i !== -1) $scope.tournaments[i] = angular.copy($scope.tournament);
      } else {                                          // create
          $scope.tournament.id = Date.now();
          $scope.tournaments.push(angular.copy($scope.tournament));
      }
      $scope.cancelEdit();

      // if ($scope.tournament.id) {
      //   // Обновление существующего
      //   $http.put(`/api/tournaments/${$scope.tournament.id}`, $scope.tournament)
      //     .then(() => $scope.cancelEdit())
      //     .catch(err => console.error("Error updating tournament:", err));
      // } else {
      //   // Добавление нового
      //   $http.post('/api/tournaments', $scope.tournament)
      //     .then(response => {
      //       $scope.tournaments.push(response.data);
      //       $scope.cancelEdit();
      //     })
      //     .catch(err => console.error("Error creating tournament:", err));
      // }

    };



    $scope.deleteTournament = () => {
        if (confirm('Delete "' + $scope.tournament.name + '" ?')) {
            $scope.tournaments = $scope.tournaments.filter(t => t.id !== $scope.tournament.id);
            $scope.cancelEdit();
        }
        // $http.delete(`/api/tournaments/${$scope.tournament.id}`)
        //   .then(() => {
        //     $scope.tournaments = $scope.tournaments.filter(t => t.id !== $scope.tournament.id);
        //     $scope.cancelEdit();
        //   })
        //   .catch(err => console.error("Error deleting tournament:", err));
    };

    $scope.cancelEdit = () => {
      $scope.tournament = {};
      $scope.fight = {};
      $scope.mode = 'list';
    };

    $scope.goToFightEdit = () => {
        $scope.mode = 'fightEdit';
    };


    // Fight card methods
    $scope.addFight = () => {
      $scope.fight = {
        id: Date.now(),  // 🆕 уникальный ID сразу
        fighter1: '',
        fighter2: '',
        result: 'vs.',
        weightClass: $scope.weightClasses[0]
      };
      $scope.mode = 'fightEdit';
    };

    $scope.editFight = fight => {
      $scope.fight = angular.copy(fight);
      $scope.mode = 'fightEdit';
    };


    $scope.saveFight = () => {
      const idx = $scope.tournament.fights.findIndex(f => f.id === $scope.fight.id);
      if (idx !== -1) {
        $scope.tournament.fights[idx] = angular.copy($scope.fight);
      } else {
        $scope.tournament.fights.push(angular.copy($scope.fight));
      }
      $scope.fight = {};
      $scope.mode = 'edit';

      // const url = `/api/tournaments/${$scope.tournament.id}/fights`;
      // if ($scope.fight.id) {
      //   $http.put(`${url}/${$scope.fight.id}`, $scope.fight)
      //     .then(() => $scope.refreshTournament())
      //     .catch(err => console.error("Error updating fight:", err));
      // } else {
      //   $http.post(url, $scope.fight)
      //     .then(response => {
      //       $scope.tournament.fights.push(response.data);
      //       $scope.fight = {};
      //       $scope.mode = 'edit';
      //     })
      //     .catch(err => console.error("Error creating fight:", err));
      // }
    };

    $scope.deleteFight = (fight) => {
      if (confirm(`Delete fight: ${fight.fighter1} ${fight.result} ${fight.fighter2}?`)) {
        $scope.tournament.fights = $scope.tournament.fights.filter(f => f.id !== fight.id);
      }
      // $http.delete(`/api/tournaments/${$scope.tournament.id}/fights/${fight.id}`)
      //   .then(() => {
      //     $scope.tournament.fights = $scope.tournament.fights.filter(f => f.id !== fight.id);
      //   })
      //   .catch(err => console.error("Error deleting fight:", err));
    };

    $scope.cancelFightEdit = () => {
      $scope.fight = {};
      $scope.mode = 'edit'; // ⬅️ Вернуться обратно
    };

    $scope.logout = () => {
      $scope.username = '';
      $scope.role = '';
    };

  });
