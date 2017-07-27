public int ladderLength(String start, String end, Set<String> dict){
        if (start == null || end == null || start.equals(end)
        || start.length() != end.length())
        return 0;

        if (isOneWordDiff(start, end))
        return 2;

        Queue<String> queue=new LinkedList<String>();
        HashMap<String,Integer> dist=new HashMap<String,Integer>();

        queue.add(start);
        dist.put(start, 1);

        while(!queue.isEmpty())
        {
        String head=queue.poll();

        int headDist=dist.get(head);
        //从每一个位置开始替换成a~z
        for(int i=0;i<head.length();i++)
        {
        for(char j='a';j<'z';j++)
        {
        if(head.charAt(i)==j) continue;
        char[] s = head.toCharArray();
        s[i]=j;
        String sb = new String(s);
        if(sb.toString().equals(end)) return headDist+1;

        if(dict.contains(sb.toString())&&!dist.containsKey(sb.toString()))
        {
        queue.add(sb.toString());
        dist.put(sb.toString(), headDist+1);
        }
        }
        }
        }

        return 0;
        }

private boolean isOneWordDiff(String a, String b) {
        int diff = 0;
        for (int i = 0; i < a.length(); i++) {
        if (a.charAt(i) != b.charAt(i)) {
        diff++;
        if (diff >= 2)
        return false;
        }
        }

        return diff == 1;
        }