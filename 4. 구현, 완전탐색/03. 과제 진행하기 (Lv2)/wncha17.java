
/*
 * [시간복잡도]
 * 정렬: O(nlogn)
 * 메인 반복: 각 과제는 스택에 최대 한 번 push, 한 번 pop -> 전체 O(n)
 * 전체: O(nlogn)
*/

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Deque;
import java.util.List;

class Solution {

    // 멈춘 과제를 담기 위한 클래스: 이름 + 남은 시간
    // (남은 시간은 이어서 진행할 때 계속 줄어들어야 하므로 변경 가능한 필드로 둠)
    static class Task {
        String name;
        int remain;

        Task(String name, int remain) {
            this.name = name;
            this.remain = remain;
        }
    }

    public String[] solution(String[][] plans) {
        int n = plans.length;

        // [1] 시작 시각 기준 오름차순 정렬
        // 배열이 시간순이 아닐 수 있으므로, 실제로 시작하는 순서대로 다시 줄 세움
        Arrays.sort(plans, (a, b) -> Integer.compare(toMinutes(a[1]), toMinutes(b[1])));

        // 끝낸 과제 이름을 순서대로 담을 리스트
        List<String> answer = new ArrayList<>();

        // 멈춘 과제 보관함 = 스택 (가장 최근에 멈춘 과제가 맨 위)
        Deque<Task> paused = new ArrayDeque<>();

        // [2] 마지막 과제 전까지: "현재 과제 <-> 다음 과제" 사이 간격으로 판단
        for (int i = 0; i < n - 1; i++) {
            String name = plans[i][0];
            int start = toMinutes(plans[i][1]);
            int playtime = Integer.parseInt(plans[i][2]);
            int nextStart = toMinutes(plans[i + 1][1]);

            // 현재 과제를 시작해서 다음 과제가 시작되기 전까지 쓸 수 있는 시간
            int gap = nextStart - start;

            if (playtime <= gap) {
                // (경우 A: 다음 과제 시작 전에 현재 과제를 끝냄)
                answer.add(name);

                // 현재 과제를 끝내고도 남는 시간
                int free = gap - playtime;

                // 남는 시간 동안 멈춘 과제들을 "가장 최근 것부터" 이어서 진행
                // free가 0이면 바로 다음 과제가 시작되므로 멈춘 과제는 진행하지 않음
                // -> "새 과제와 멈춘 과제가 동시에 있으면 새 과제 먼저" 규칙이 자동으로 지켜짐
                while (free > 0 && !paused.isEmpty()) {
                    Task top = paused.peek();   // 가장 최근에 멈춘 과제

                    if (top.remain <= free) {
                        // 남은 시간 안에 이 과제를 끝낼 수 있음
                        free -= top.remain;
                        paused.pop();
                        answer.add(top.name);
                        // 아직 free가 남았다면 그 다음 멈춘 과제로 계속 진행
                    } else {
                        // 다 못 끝냄 -> 진행한 만큼만 남은 시간을 줄이고 다시 멈춤
                        // (스택 맨 위에 그대로 있으므로 여전히 "가장 최근에 멈춘 과제")
                        top.remain -= free;
                        free = 0;
                    }
                }
            } else {
                // (경우 B: 다음 과제 시작 때문에 현재 과제를 멈춤)
                // gap만큼 진행했으므로 남은 시간 = playtime - gap
                paused.push(new Task(name, playtime - gap));
            }
        }

        // [3] 마지막 과제: 뒤에 시작할 과제가 없으므로 방해받지 않고 끝남
        answer.add(plans[n - 1][0]);

        // [4] 남아 있는 멈춘 과제들을 가장 최근에 멈춘 것부터 차례로 끝냄
        while (!paused.isEmpty()) {
            answer.add(paused.pop().name);
        }

        return answer.toArray(new String[0]);
    }

    // "HH:MM" -> 자정부터 몇 분 지났는지
    private int toMinutes(String time) {
        String[] t = time.split(":");
        return Integer.parseInt(t[0]) * 60 + Integer.parseInt(t[1]);
    }
}
