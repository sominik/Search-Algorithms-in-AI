
public class test {

    public static void main(String[] args) {
        Player player = new Player(0, 1, 100, 100);
        Visualizer visualizer = new Visualizer();
        //indexes start from zero
        System.out.println("player position: " + player.i + " " + player.j);
        Map map = new Map(7, 6);
        //initialization of the map
        //G for normal ground
        //S for swamp
        //K for key
        //C for castle
        //L for Loot
        //B for bandit
        //W for wildAnimals
        //P for bridge
        map.addEntity(0, 0, new Bandit(50));
        map.addEntity(0, 1, new BaseEntity('G'));
        map.addEntity(0, 2, new BaseEntity('S'));
        map.addEntity(0, 3, new Bandit(150));
        map.addEntity(0, 4, new BaseEntity('G'));
        map.addEntity(0, 5, new BaseEntity('G'));
        map.addEntity(1, 0, new BaseEntity('S'));
        map.addEntity(1, 1, new Bridge());
        map.addEntity(1, 2, new BaseEntity('G'));
        map.addEntity(1, 3, new BaseEntity('S'));
        map.addEntity(1, 4, new Bridge());
        map.addEntity(1, 5, new Bandit(50));
        map.addEntity(2, 0, new Loot(50, 50));
        map.addEntity(2, 1, new BaseEntity('G'));
        map.addEntity(2, 2, new WildAnimall(150));
        map.addEntity(2, 3, new BaseEntity('G'));
        map.addEntity(2, 4, new BaseEntity('G'));
        map.addEntity(2, 5, new BaseEntity('G'));
        map.addEntity(3, 0, new BaseEntity('G'));
        map.addEntity(3, 1, new WildAnimall(50));
        map.addEntity(3, 2, new Loot(50, 50));
        map.addEntity(3, 3, new BaseEntity('G'));
        map.addEntity(3, 4, new Bandit(150));
        map.addEntity(3, 5, new BaseEntity('S'));
        map.addEntity(4, 0, new BaseEntity('G'));
        map.addEntity(4, 1, new Bandit(50));
        map.addEntity(4, 2, new BaseEntity('G'));
        map.addEntity(4, 3, new BaseEntity('G'));
        map.addEntity(4, 4, new BaseEntity('C'));
        map.addEntity(4, 5, new WildAnimall(200));
        map.addEntity(5, 0, new BaseEntity('S'));
        map.addEntity(5, 1, new BaseEntity('G'));
        map.addEntity(5, 2, new Loot(100, 100));
        map.addEntity(5, 3, new BaseEntity('G'));
        map.addEntity(5, 4, new BaseEntity('S'));
        map.addEntity(5, 5, new BaseEntity('G'));
        map.addEntity(6, 0, new Bandit(100));
        map.addEntity(6, 1, new BaseEntity('K'));
        map.addEntity(6, 2, new BaseEntity('S'));
        map.addEntity(6, 3, new BaseEntity('G'));
        map.addEntity(6, 4, new BaseEntity('S'));
        map.addEntity(6, 5, new BaseEntity('S'));
        //print method prints the map
        map.print();
        visualizer.printMap(map, player);
        Node node = new Node(player, map, null, null);
//        node.level=0;
//        BFS bfs = new BFS();
//        bfs.search(node);
//        DFS dfs = new DFS();
//        dfs.search(node);
//        IDS ids=new IDS();
//        ids.search(node);
//        BDS bds=new BDS();
//        bds.search(node);
//        A_star a_star = new A_star();
//        a_star.search(node);
//        IDA_star ida_star = new IDA_star();
//        ida_star.search(node);

        RBFS rbfs = new RBFS();
        rbfs.search(node);
    }
}
