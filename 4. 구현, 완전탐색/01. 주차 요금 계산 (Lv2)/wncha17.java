
/*
 * [시간복잡도]
 * '기록 수를 R(<= 1,000), 차량 수를 C(<= R)라 할 때'
 * 기록 처리 (HashMap 연산 + TreeMap 갱신): O(RlogC)
 * 남은 차량 처리                        : O(ClogC)
 * 요금 계산                            : O(C)
 * 전체                                : O(RlogC)
*/

import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

class Solution {
    public int[] solution(int[] fees, String[] records) {

        // [0] 요금표를 이름 있는 변수로 꺼내 두기 (가독성 + 실수 방지)
        int baseTime = fees[0]; // 기본 시간(분)
        int baseFee = fees[1];  // 기본 요금(원)
        int unitTime = fees[2]; // 단위 시간(분)
        int unitFee = fees[3];  // 단위 요금(원)

        // 현재 주차장 안에 있는 차량 -> 입차 시각(분)
        Map<String, Integer> inTime = new HashMap<>();

        // 차량별 누적 주차 시간(분), 차량 번호 오름차순으로 자동 정렬
        Map<String, Integer> totalTime = new TreeMap<>();

        // [1] 기록을 하나씩 읽으며 누적 주차 시간 계산
        for (String record : records) {
            // "05:34 5961 IN" -> ["05:34", "5961", "IN"]
            String[] parts = record.split(" ");
            int time = toMinutes(parts[0]); // 시각을 분으로 변환
            String car = parts[1];          // 차량 번호 (앞자리 0 유지를 위해 문자열 그대로)
            String type = parts[2];         // "IN" 또는 "OUT"

            if (type.equals("IN")) {
                // 입차: 들어온 시각만 기록해 둠
                inTime.put(car, time);
            } else {
                // 출차: 들어온 시각을 꺼내고(삭제) 이번 주차 시간을 누적
                //   remove()는 값을 돌려주면서 동시에 맵에서 지워줌
                //   -> 지워야 나중에 "아직 안 나간 차"를 구분할 수 있음
                int parked = time - inTime.remove(car);
                totalTime.put(car, totalTime.getOrDefault(car, 0) + parked);
            }
        }

        // [2] 출차 기록이 없는 차량은 23:59에 출차한 것으로 처리
        // [1]에서 OUT 때마다 지웠으므로, inTime에 남아 있는 차 = 아직 안 나간 차
        int endOfDay = 23 * 60 + 59;  // 23:59 = 1439분
        for (Map.Entry<String, Integer> entry : inTime.entrySet()) {
            String car = entry.getKey();
            int parked = endOfDay - entry.getValue();
            totalTime.put(car, totalTime.getOrDefault(car, 0) + parked);
        }

        // [3] 누적 시간으로 요금 계산 (TreeMap이라 차량 번호 순서로 순회됨)
        int[] answer = new int[totalTime.size()];
        int idx = 0;

        for (int minutes : totalTime.values()) {
            answer[idx++] = calcFee(minutes, baseTime, baseFee, unitTime, unitFee);
        }

        return answer;
    }

    // 누적 주차 시간(분)으로 요금 계산
    private int calcFee(int minutes, int baseTime, int baseFee, int unitTime, int unitFee) {
        // 기본 시간 이하 -> 기본 요금만
        if (minutes <= baseTime) {
            return baseFee;
        }

        // 기본 시간 초과 -> 초과 시간을 단위 시간으로 나눈 값을 "올림"
        int extra = minutes - baseTime;

        // 정수 올림 공식: (extra + unitTime - 1) / unitTime
        // 또는 Math.ceil((double) extra / unitTime)
        int units = (extra + unitTime - 1) / unitTime;

        return baseFee + units * unitFee;
    }

    // "HH:MM" -> 자정부터 몇 분 지났는지
    private int toMinutes(String hhmm) {
        String[] t = hhmm.split(":");
        return Integer.parseInt(t[0]) * 60 + Integer.parseInt(t[1]);
    }
}
