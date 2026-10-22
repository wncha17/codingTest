
// [시간복잡도]
// O(3^G * 5)

class Solution {

    // COST[곡괭이][광물] = 피로도
    static final int[][] COST = {
        {1, 1, 1},
        {5, 1, 1},
        {25, 5, 1}
    };

    int[] mineralType;      // 광물을 0/1/2 숫자로 바꿔 둔 배열
    int answer = Integer.MAX_VALUE;

    public int solution(int[] picks, String[] minerals) {

        // [1] 광물 문자열을 숫자로 변환 (매번 문자열 비교하지 않도록)
        mineralType = new int[minerals.length];
        for (int i = 0; i < minerals.length; i++) {
            switch (minerals[i]) {
                case "diamond": mineralType[i] = 0; break;
                case "iron":    mineralType[i] = 1; break;
                default:        mineralType[i] = 2; break; // "stone"
            }
        }

        // [2] 0번 광물부터, 피로도 0으로 탐색 시작
        dfs(picks, 0, 0);
        return answer;
    }

    /*
     * picks    : 이번에 캘 묶음의 첫 번째 광물 위치 (0, 5, 10, ...)
     * idx      : 남은 곡괭이 수 [다이아, 철, 돌]
     * fatigue  : 지금까지 쌓인 피로도
    */
    void dfs(int[] picks, int idx, int fatigue) {

        // [가지치기] 이미 찾은 최솟값 이상이면 더 내려가 봐야 의미 없음
        if (fatigue >= answer) return;

        // [종료 조건] 광물을 다 캤거나, 남은 곡괭이가 하나도 없으면 작업 끝
        if (idx >= mineralType.length || picks[0] + picks[1] + picks[2] == 0) {
            answer = fatigue;
            return;
        }

        // [선택] 남아 있는 곡괭이 3종류를 하나씩 이번 묶음에 써 봄
        for (int pick = 0; pick < 3; pick++) {
            if (picks[pick] == 0) continue;     // 이 종류는 다 썼으면 건너뜀

            // 이 곡괭이로 idx부터 최대 5개 캤을 때의 피로도
            // (마지막 묶음은 5개가 안 될 수 있으므로 배열 끝을 넘지 않게 함)
            int cost = 0;
            int end = Math.min(idx + 5, mineralType.length);
            for (int i = idx; i < end; i++) {
                cost += COST[pick][mineralType[i]];
            }

            picks[pick]--;                          // 곡괭이 하나 사용
            dfs(picks, idx + 5, fatigue + cost);    // 다음 묶음으로
            picks[pick]++;                          // 되돌리기(백트래킹): 다른 선택지 시도 위함
        }
    }
}
