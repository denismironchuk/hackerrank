package kickstart.year2022;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;
import java.util.stream.IntStream;

public class SchedulingAMeeting {

    private static class Meeting {
        private int startTime;
        private int endTime;
        private int techLead;

        public Meeting(int startTime, int endTime, int techLead) {
            this.startTime = startTime;
            this.endTime = endTime;
            this.techLead = techLead;
        }
    }

    private static class LeadsMeetings implements Comparable<LeadsMeetings> {
        private int lead;
        private int meetingCnt;

        public LeadsMeetings(int lead, int meetingCnt) {
            this.lead = lead;
            this.meetingCnt = meetingCnt;
        }

        @Override
        public int compareTo(LeadsMeetings o) {
            int cmp1 = Integer.compare(meetingCnt, o.meetingCnt);
            if (cmp1 == 0) {
                return Integer.compare(lead, o.lead);
            }
            return cmp1;
        }
    }

    public static void main(String[] args) throws IOException {
        try (BufferedReader br = new BufferedReader(new InputStreamReader(System.in))) {
            int T = Integer.parseInt(br.readLine());
            for (int t = 1; t <= T; t++) {
                StringTokenizer tkn1 = new StringTokenizer(br.readLine());
                int n = Integer.parseInt(tkn1.nextToken()); //leads total count
                int k = Integer.parseInt(tkn1.nextToken()); //minimal approval needed
                int x = Integer.parseInt(tkn1.nextToken()); //required duration
                int d = Integer.parseInt(tkn1.nextToken()); //timeline length
                int m = Integer.parseInt(br.readLine());
                List<Meeting> meetings = new ArrayList<>();
                for (int i = 0; i < m; i++) {
                    StringTokenizer tkn2 = new StringTokenizer(br.readLine());
                    int p = Integer.parseInt(tkn2.nextToken()) - 1;
                    int l = Integer.parseInt(tkn2.nextToken());
                    int r = Integer.parseInt(tkn2.nextToken());
                    meetings.add(new Meeting(l, r, p));
                }
                Map<Integer, List<Meeting>> startPoints = new HashMap<>();
                Map<Integer, List<Meeting>> endPoints = new HashMap<>();
                for (Meeting meeting : meetings) {
                    if (!startPoints.containsKey(meeting.startTime)) {
                        startPoints.put(meeting.startTime, new ArrayList<>());
                    }
                    startPoints.get(meeting.startTime).add(meeting);

                    if (!endPoints.containsKey(meeting.endTime)) {
                        endPoints.put(meeting.endTime, new ArrayList<>());
                    }
                    endPoints.get(meeting.endTime).add(meeting);
                }

                TreeSet<LeadsMeetings> leadsMeetings = new TreeSet<>();
                LeadsMeetings[] leadsMeetingList = new LeadsMeetings[n];
                for (int i = 0; i < n; i++) {
                    leadsMeetingList[i] = new LeadsMeetings(i, 0);
                }
                for (int time = 0; time < x; time++) {
                    List<Meeting> startingMeetings = startPoints.get(time);
                    if (startingMeetings != null) {
                        for (Meeting startingMeeting : startingMeetings) {
                            LeadsMeetings leadsMeeting = leadsMeetingList[startingMeeting.techLead];
                            leadsMeetings.remove(leadsMeeting);
                            leadsMeeting.meetingCnt++;
                            leadsMeetings.add(leadsMeeting);
                        }
                    }
                }
                int busyLeadsCnt = leadsMeetings.size();
                int freeLeadsCnt = n - busyLeadsCnt;
                if (freeLeadsCnt >= k) {
                    System.out.printf("Case #%s: 0\n", t);
                    continue;
                }
                Iterator<LeadsMeetings> itr = leadsMeetings.iterator();
                int res = IntStream.range(0, k - freeLeadsCnt)
                        .mapToObj(i -> itr.next().meetingCnt).reduce(0, (a, b) -> a + b);

                for (int startTime = 1; startTime + x <= d; startTime++) {
                    List<Meeting> endingMeetings = endPoints.get(startTime);
                    if (endingMeetings != null) {
                        for (Meeting startingMeeting : endingMeetings) {
                            LeadsMeetings leadsMeeting = leadsMeetingList[startingMeeting.techLead];
                            leadsMeetings.remove(leadsMeeting);
                            leadsMeeting.meetingCnt--;
                            leadsMeetings.add(leadsMeeting);
                        }
                    }
                    List<Meeting> startingMeetings = startPoints.get(startTime + x - 1);
                    if (startingMeetings != null) {
                        for (Meeting startingMeeting : startingMeetings) {
                            LeadsMeetings leadsMeeting = leadsMeetingList[startingMeeting.techLead];
                            leadsMeetings.remove(leadsMeeting);
                            leadsMeeting.meetingCnt++;
                            leadsMeetings.add(leadsMeeting);
                        }
                    }

                    busyLeadsCnt = leadsMeetings.size();
                    freeLeadsCnt = n - busyLeadsCnt;
                    if (freeLeadsCnt >= k) {
                        res = 0;
                        break;
                    }

                    Iterator<LeadsMeetings> itr1 = leadsMeetings.iterator();
                    int candidate = IntStream.range(0, k - freeLeadsCnt)
                            .mapToObj(i -> itr1.next().meetingCnt).reduce(0, (a, b) -> a + b);
                    res = Math.min(candidate, res);
                }

                System.out.printf("Case #%s: %s\n", t, res);
            }
        }
    }
}