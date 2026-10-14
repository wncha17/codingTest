
/*
 * [시간복잡도]
 * 변환: O(n)
 * 정렬: O(nlogn)
 * 힙 처리: 손님마다 O(logn) -> O(nlogn)
 * 전체: O(nlogn)
*/

import java.util.Arrays;
import java.util.PriorityQueue;

class Solution {
    public int solution(String[][] book_time) {
        int n = book_time.length;

        // [1] 시각 문자열을 "분" 단위 정수로 변환
        // times[i] = {입실 시각(분), 방이 다시 비는 시각(분)}
        int [][] times = new int[n][2];
        for (int i = 0; i < n; i++) {
            times[i][0] = toMinutes(book_time[i][0]);       // 입실
            times[i][1] = toMinutes(book_time[i][1]) + 10;  // 퇴실 + 청소 10분
        }

        // [2] 입실 시각이 빠른 순서로 정렬
        // 프런트에 도착하는 순서대로 손님을 처리하기 위함
        Arrays.sort(times, (x, y) -> Integer.compare(x[0], y[0]));

        // [3] 사용 중인 방들의 "다시 비는 시각"을 담는 최소 힙
        // peek() 하면 "가장 먼저 비는 방"의 시각이 나옴
        // 힙에 들어있는 원소 1개 = 방 1개
        PriorityQueue<Integer> rooms = new PriorityQueue<>();

        // [4] 손님을 한 명씩 받기
        for (int[] guest : times) {
            int start = guest[0];   // 이 손님의 입실 시각
            int end = guest[1];     // 이 손님이 쓴 방이 다시 비는 시각

            // 가장 먼저 비는 방이, 이 손님 입실 시각까지 비어 있다면 그 방을 재사용
            // (<= 인 이유: 청소가 끝나는 시각과 입실 시각이 같아도 바로 들어갈 수 있음)
            if (!rooms.isEmpty() && rooms.peek() <= start) {
                rooms.poll();   // 이전 손님의 기록을 지우고 (같은 방을 이 손님에게 넘겨줌)
            }

            // 이 손님이 쓰는 방의 "다시 비는 시각"을 기록
            //  - 위에서 poll() 했다면: 기존 방을 재사용한 것 -> 방 개수 그대로
            //  - poll() 안 했다면: 새 방을 연 것 -> 방 개수 +1
            rooms.add(end);
        }

        // [5] 힙의 크기 = 지금까지 연 방의 개수 = 필요한 최소 객실 수
        // 손님마다 add는 항상 1번, poll은 최대 1번이므로
        // 힙 크기는 절대 줄어들지 않음 -> 마지막 크기가 곧 최대 동시 사용 방 수
        return rooms.size();
    }

    private int toMinutes(String time) {
        String[] parts = time.split(":");
        int hour = Integer.parseInt(parts[0]);
        int minute = Integer.parseInt(parts[1]);
        return hour * 60 + minute;
    }
}
