
/*
 * [시간복잡도]
 * 라운드마다 힙에 넣기/빼기 1번씩: O(logk)
 * 전체: O(mlogk) (m: enemy 길이)
*/

import java.util.PriorityQueue;

class Solution {
    public int solution(int n, int k, int[] enemy) {

        // [0] 무적권이 라운드 수 이상이면 모든 라운드를 무적권으로 막을 수 있음
        if (k >= enemy.length) {
            return enemy.length;
        }

        // [1] 무적권 예약 목록 (최소 힙)
        // 지금까지 나온 라운드 중 "무적권으로 막기로 한 라운드들의 적 수"를 담늗다.
        // 기본 PriorityQueue는 작은 값이 먼저 나오므로,
        // peek()/poll() 하면 "예약 중 가장 아까운(적이 가장 적은) 라운드"가 나온다.
        PriorityQueue<Integer> shield = new PriorityQueue<>();

        // [2] 라운드를 순서대로 진행
        for (int i = 0; i < enemy.length; i++) {
            
            // (1) 이번 라운드도 일단 무적권 예약 목록에 넣어 봄
            shield.add(enemy[i]);

            // (2) 예약이 무적권 개수(k)를 넘었다면
            //     -> 예약 중 가장 적이 적은 라운드 하나를 빼서 병사로 막은 것으로 처리
            //     (이렇게 하면 무적권은 항상 "지금까지 가장 큰 k개 라운드"에 쓰인 상태가 유지됨)
            if (shield.size() > k) {
                n -= shield.poll(); // 가장 적은 적 수만큼 병사 소모
            }

            // (3) 병사가 음수가 되었다면, 이번 라운드(i+1번째)는 막을 수 없음
            //     -> 지금까지 막은 라운드 수는 i개 (0번 ~ i-1번 라운드)
            if (n < 0) {
                return i;
            }
        }

        // [3] 끝까지 병사가 버텼다면 모든 라운드를 막은 것
        return enemy.length;
    }
}
