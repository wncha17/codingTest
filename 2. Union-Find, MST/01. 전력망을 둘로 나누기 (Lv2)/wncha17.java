
/*
 * [시간복잡도]
 * 끊을 전선 선택: n-1번
 * 각 시도: 초기화 + union + 크기 세기: O(n)
 * 전체: O(n*n)
*/

class Solution {

    // parent[i] = i번 송전탑의 바로 위 노드 (parent[i] == i 이면 그룹의 대표)
    static int[] parent;

    public int solution(int n, int[][] wires) {
        int answer = Integer.MAX_VALUE;

        // [1] 끊을 전선을 하나씩 정해서 모두 시도
        for (int cut = 0; cut < wires.length; cut++) {

            // (1) Union-Find 초기화
            // 시도할 때마다 "아무것도 연결 안 된 상태"에서 새로 시작해야 함
            // (이전 시도의 연결 기록이 남으면 끊은 전선이 연결된 것처럼 계산됨)
            parent = new int[n + 1];
            for (int i = 1; i <= n; i++) {
                parent[i] = i;
            }

            // (2) 끊은 전선(cut번)만 빼고 나머지 전선을 모두 연결
            for (int i = 0; i < wires.length; i++) {
                if (i == cut) continue;         // 이 전선은 끊었으므로 연결하지 않음
                union(wires[i][0], wires[i][1]);
            }

            // (3) 끊은 전선의 한쪽 끝이 속한 덩어리의 크기 세기
            // 끊긴 전선의 양 끝은 반드시 서로 다른 덩어리에 속함
            // -> 한쪽 끝(wires[cut][0])의 대표와 같은 대표를 가진 송전탑 수 = 그 덩어리 크기
            int root = find(wires[cut][0]);
            int size = 0;
            for (int i = 1; i <= n; i++) {
                if (find(i) == root) {
                    size++;
                }
            }

            // (4) 두 덩어리의 크기 차이 계산
            // 한쪽이 size개면 다른 쪽은 (n - size)개
            // 차이 = |size - (n - size)| = |n - 2 * size|
            int diff = Math.abs(n - 2 * size);
            answer = Math.min(answer, diff);
        }

        return answer;
    }

    // Find: x가 속한 그룹의 대표를 반환 (경로 압축)
    static int find(int x) {
        if (parent[x] == x) {
            return x;
        }
        parent[x] = find(parent[x]);    // 찾은 대표를 바로 가리키도록 압축
        return parent[x];
    }

    // Union: a의 그룹과 b의 그룹을 하나로 합침
    // n <= 100으로 작아서 랭크 기준 합치기 없이 경로 압축만으로 충분
    static void union(int a, int b) {
        int rootA = find(a);
        int rootB = find(b);
        if (rootA != rootB) {
            parent[rootB] = rootA;  // 반장끼리 연결
        }
    }
}
