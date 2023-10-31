

import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

public class BDS {

    Visualizer visualizer = new Visualizer();
        public boolean isGoal(Node[] nodes,int counter, Node node, String str) {
        if (str.equalsIgnoreCase("initial to goal")) {
            for (int i = 0; i < counter; i++) {
                if (isEqualStates(node, nodes[i])) {
                    return true;
                }
            }
        } else if (str.equalsIgnoreCase("goal to initial")) {
            for (int i = 0; i < counter; i++) {
                if (isEqualStates(nodes[i], node)) {
                    return true;
                }
            }
        }
        return false;
    }

    public boolean isEqualStates(Node node1, Node node2) {
        if (node1.player.i == node2.player.i && node1.player.j == node2.player.j) {
            if (node1.player.haskey == node2.player.haskey) {
                BaseEntity entity = node1.map.at(node1.player.i, node1.player.j);
                if (entity.name == 'B') {
                    Bandit bandit = (Bandit) entity;
                    if (node2.player.money - node1.player.money == bandit.power && node1.player.food == node2.player.food) {
                        return true;
                    }
                } else if (entity.name == 'W') {
                    WildAnimall wildAnimall = (WildAnimall) entity;
                    if (node2.player.food - node1.player.food == wildAnimall.power && node1.player.money == node2.player.money) {
                        return true;
                    }
                } else if (entity.name == 'L') {
                    Loot loot = (Loot) entity;
                    if ((node1.player.money - node2.player.money == loot.money && node2.player.food == node1.player.food)
                            || (node1.player.food - node2.player.food == loot.food && node2.player.money == node1.player.money)) {
                        return true;
                    }
                } else if (node1.player.money == node2.player.money && node1.player.food == node2.player.food) {
                    return true;
                }
            }
        }
        return false;
    }

    public Node GoalNode(Node intialNode) {
        Queue<Node> frontier = new LinkedList<Node>();
        Hashtable<String, Boolean> inFrontier = new Hashtable<>();
        Hashtable<String, Boolean> explored = new Hashtable<>();
        if (isGoal(intialNode)) {
            return intialNode;
        }
        frontier.add(intialNode);
        inFrontier.put(intialNode.hash(), true);
        while (!frontier.isEmpty()) {
            Node temp = frontier.poll();
            inFrontier.remove(temp.hash());
            explored.put(temp.hash(), true);
            ArrayList<Node> children = temp.successor();
            for (int i = 0; i < children.size(); i++) {
                if (!(inFrontier.containsKey(children.get(i).hash())) && !(explored.containsKey(children.get(i).hash()))) {
                    if (isGoal(children.get(i))) {
                        return children.get(i);
                    }
                    frontier.add(children.get(i));
                    inFrontier.put(children.get(i).hash(), true);
                }
            }
        }
        return null;
    }
    public void search(Node initialNode) {
        Queue<Node> frontier = new LinkedList<>();
        Hashtable<String, Boolean> inFrontier = new Hashtable<>();
        Hashtable<String, Node> explored = new Hashtable<>();
        Queue<Node> reversedFrontier = new LinkedList<>();
        Hashtable<String, Boolean> reversedInFrontier = new Hashtable<>();
        Hashtable<String, Node> reversedExplored = new Hashtable<>();
        
        if (isGoal(initialNode)) {
            result(initialNode);
            return;
        }
        frontier.add(initialNode);
        inFrontier.put(initialNode.hash(), true);
        
        Node goalNode = this.GoalNode(initialNode);
        boolean isGoalNodeFound = true;
        if(goalNode==null){
            isGoalNodeFound=false;
        }
        if (isGoalNodeFound) {
            reversedFrontier.add(goalNode);
            reversedInFrontier.put(goalNode.hash(), true);
        }

        // BDS
        while (!(reversedFrontier.isEmpty() || frontier.isEmpty())) {

            // Forward
            Node temp = frontier.poll();
            inFrontier.remove(temp.hash());
            explored.put(temp.hash(), temp);
            ArrayList<Node> children = temp.successor();
             for (Node child : children) {
                if (!(inFrontier.containsKey(child.hash())) && !(explored.containsKey(child.hash()))) {
                    if (reversedExplored.containsKey(child.hash())) {
                        result(reversedExplored.get(child.hash()));
                        return;
                    }
                    frontier.add(child);
                    inFrontier.put(child.hash(), true);
                }
            }

            // Backward
            Node revTemp = reversedFrontier.poll();
            reversedInFrontier.remove(revTemp.hash());
            reversedExplored.put(revTemp.hash(), revTemp);
            ArrayList<Node> revChildren = revTemp.reversed_successor();
            for (Node revChild : revChildren) {
                if (!(reversedInFrontier.contains(revChild.hash())) && !(reversedExplored.containsKey(revChild.hash()))) {
                    if (explored.containsKey(revChild.hash())) {
                        result(revChild);
                        return;
                    }
                    reversedFrontier.add(revChild);
                    reversedInFrontier.put(revChild.hash(), true);
                }
            }

           

        }

    }

    public boolean isGoal(Node node) {
        if (node.map.at(node.player.i, node.player.j).name == 'C') {
            return true;
        } else {
            return false;
        }
    }

    public void result(Node node) {
        Stack<Node> nodes = new Stack<>();
        while (true) {
            nodes.push(node);
            if (node.parentNode == null) {
                break;
            } else {
                node = node.parentNode;
            }
        }
        nodes.pop();
        try {
            FileWriter myWriter = new FileWriter("result4.txt");
            while (!nodes.empty()) {
                Node tempNode = nodes.pop();
                String action = tempNode.priviousAction;
                System.out.println(action + " " + tempNode.player.money + " " + tempNode.player.food);
                myWriter.write(action + "\n");
                if (isGoal(tempNode)) {
                    break;
                }
                //print visualized map for every movement
                visualizer.printMap(tempNode.map, tempNode.player);
            }
            myWriter.close();
            System.out.println("Successfully wrote to the file.");
        } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
    }
}
