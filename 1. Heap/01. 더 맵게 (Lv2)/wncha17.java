
/*
 * [시간복잡도]
 * 힙에 n개 넣기: O(nlogn)
 * 섞기 반복: O(nlogn)
*/

import java.util.PriorityQueue;

class Solution {
    public int solution(int[] scoville, int K) {
        // 가장 매운(값이 큰) 것이 아니라, 가장 맵지 않은(값이 작은) 것을 우선적으로 꺼내야 하므로
        // 자바 PriorityQueue의 기본 동작(최솟값 우선)을 그대로 사용
        PriorityQueue<Integer> pq = new PriorityQueue<>();
        for (int s : scoville) {
            pq.add(s);
        }
        
        int count = 0;
        
        // 가장 맵지 않은 음식이 K 이상이 될 때까지 반복
        while (pq.peek() < K) {
            // 더 이상 섞을 음식이 2개 미만이면, 모든 음식을 K 이상으로 만들 수 없음
            if (pq.size() < 2) {
                return -1;
            }
            
            int first = pq.poll();  // 가장 맵지 않은 음식
            int second = pq.poll(); // 두 번째로 맵지 않은 음식
            
            int mixed = first + second * 2;
            pq.add(mixed);
            
            count++;
        }
        
        return count;
    }
}
