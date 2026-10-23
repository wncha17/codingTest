// import java.util.LinkedList;
// import java.util.Queue;

// class Solution {
//     public int solution(int[][] maps) {
//         int n = maps.length;    // 행 크기
//         int m = maps[0].length; // 열 크기

//         // 상,하,좌,우 이동을 위한 방향벡터
//         int[] dx = {-1, 1, 0, 0};
//         int[] dy = {0, 0, -1, 1};

//         // 시작점(0,0)을 큐에 넣고 시작
//         Queue<int[]> queue = new LinkedList<>();
//         queue.add(new int[]{0, 0});

//         while (!queue.isEmpty()) {
//             int[] curr = queue.poll();
//             int x = curr[0];
//             int y = curr[1];

//             // 4방향 탐색
//             for (int i = 0; i < 4; i++) {
//                 int nx = x + dx[i];
//                 int ny = y + dy[i];

//                 // 1. 맵 범위 안에 있고
//                 // 2. 벽이 아니며 (1이고)
//                 // 3. 처음 방문한 곳이라면
//                 if (nx >= 0 && nx < n && ny >= 0 && ny < m && maps[nx][ny] == 1) {
//                     // 현재 칸까지의 거리 +1을 기록 (방문처리 겸용)
//                     maps[nx][ny] = maps[x][y] + 1;
//                     queue.add(new int[]{nx, ny});
//                 }
//             }
//         }

//         // 우측 하단 목적지(n-1, m-1)의 값을 확인
//         int answer = maps[n - 1][m - 1];
//         // 만약 값이 1이라면(벽에 막혀 도달 못한 경우) -1 반환
//         return answer > 1 ? answer : -1;
//     }
// }


import java.util.ArrayDeque;
import java.util.Arrays;

class Solution {
    public int solution(int[][] maps) {
        int n = maps.length;       // 행 수
        int m = maps[0].length;    // 열 수
        int[] dx = {-1, 1, 0, 0};
        int[] dy = {0, 0, -1, 1};

        // 거리 배열을 따로 두고, -1 = 아직 방문 안 함
        // (원본 maps는 "벽인지 아닌지" 판단에만 사용)
        int[][] dist = new int[n][m];
        for (int[] row : dist) Arrays.fill(row, -1);

        ArrayDeque<int[]> queue = new ArrayDeque<>();   // LinkedList보다 가볍고 빠름
        queue.add(new int[]{0, 0});
        dist[0][0] = 1;   // 지나간 칸 수로 세므로 출발 칸 = 1 (이제 '미방문'과 절대 겹치지 않음)

        while (!queue.isEmpty()) {
            int[] cur = queue.poll();
            int x = cur[0], y = cur[1];

            // 도착 칸을 꺼낸 순간이 최단 거리 → 바로 반환
            if (x == n - 1 && y == m - 1) return dist[x][y];

            for (int d = 0; d < 4; d++) {
                int nx = x + dx[d];
                int ny = y + dy[d];
                if (nx < 0 || nx >= n || ny < 0 || ny >= m) continue;   // 맵 밖
                if (maps[nx][ny] == 0) continue;                         // 벽
                if (dist[nx][ny] != -1) continue;                        // 이미 방문

                dist[nx][ny] = dist[x][y] + 1;
                queue.add(new int[]{nx, ny});
            }
        }
        return -1;   // 큐가 빌 때까지 도착 못 함
    }
}