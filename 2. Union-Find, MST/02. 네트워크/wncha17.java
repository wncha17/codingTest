
/*
 * [Union-Find 풀이]
 * 연결 정보를 하나씩 보며 그룹을 계속 합쳐 나감
 * '시간복잡도': O(n*n)
*/

class Solution {

    // parent[i] = i번 컴퓨터의 바로 위 노드 (parent[i] == i 이면 그 네트워크의 대표)
    int[] parent;

    public int solution(int n, int[][] computers) {

        // [1] 초기화: 처음엔 모든 컴퓨터가 혼자서 하나의 네트워크
        parent = new int[n];
        for (int i = 0; i < n; i++) {
            parent[i] = i;
        }

        // [2] 연결된 컴퓨터끼리 같은 네트워크로 합치기
        for (int i = 0; i < n; i++) {
            for (int j = i + 1; j < n; j++) {
                if (computers[i][j] == 1) {
                    union(i, j);
                }
            }
        }

        // [3] 대표의 수 세기 = 네트워크의 수
        // find(i) == i 인 컴퓨터는 자기 네트워크의 대표
        // 네트워크마다 대표는 정확히 한 명이므로, 대표 수 = 네트워크 수
        int answer = 0;
        for (int i = 0; i < n; i++) {
            if (find(i) == i) {
                answer++;
            }
        }

        return answer;
    }

    // Find: x가 속한 네트워크의 대표를 반환 (경로 압축)
    int find(int x) {
        if (parent[x] == x) {
            return x;
        }
        parent[x] = find(parent[x]);
        return parent[x];
    }

    // Union: a의 네트워크와 b의 네트워크를 하나로 합침
    void union(int a, int b) {
        int rootA = find(a);
        int rootB = find(b);
        if (rootA != rootB) {
            parent[rootB] = rootA;
        }
    }
}



/*
 * [DFS 풀이]
 * 한 컴퓨터에서 출발해 연결된 곳을 끝까지 걸어 다니며 도장을 찍음
 * '시간복잡도': O(n*n)
*/

// class Solution {
//     public int solution(int n, int[][] computers) {
//         boolean[] visited = new boolean[n];
//         int answer = 0;

//         for (int i = 0; i < n; i++) {
//             // 아직 방문하지 않은 컴퓨터를 발견하면
//             // 새로운 네트워크 하나 찾음
//             if (!visited[i]) {
//                 dfs(computers, visited, i, n);
//                 answer++;
//             }
//         }

//         return answer;
//     }

//     private void dfs(int[][] computers, boolean[] visited, int curr, int n) {
//         visited[curr] = true;

//         for (int next = 0; next < n; next++) {
//             // curr와 next가 직접 연결되어 있고, 아직 방문하지 않았다면 탐색
//             if (computers[curr][next] == 1 && !visited[next]) {
//                 dfs(computers, visited, next, n);
//             }
//         }
//     }
// }
