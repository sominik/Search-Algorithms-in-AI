
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

public class IDA_star {

    Visualizer visualizer = new Visualizer();

    public void search(Node intialNode) {
        Queue<Node> bigFrontier = new LinkedList<Node>();
        //find key & castle location:
        int keyNodeI = 0, keyNodeJ = 0, castleNodeI = 0, castleNodeJ = 0;
        for (int i = 0; i < intialNode.map.rows; i++) {
            for (int j = 0; j < intialNode.map.cols; j++) {
                if (intialNode.map.at(i, j).name == 'K') {
                    keyNodeI = i;
                    keyNodeJ = j;
                } else if (intialNode.map.at(i, j).name == 'C') {
                    castleNodeI = i;
                    castleNodeJ = j;
                }
            }
        }
        if (isGoal(intialNode)) {
            result(intialNode);
            return;
        }
        intialNode.f = this.f(intialNode, keyNodeI, keyNodeJ, castleNodeI, castleNodeJ, intialNode.g);
        int cutoff = intialNode.f;
        intialNode.g = intialNode.g + 1;

        while (true) {
            Stack<Node> frontier = new Stack<>();
            Hashtable<String, Boolean> inFrontier = new Hashtable<>();
            Hashtable<String, Boolean> explored = new Hashtable<>();
            frontier.add(intialNode);
            inFrontier.put(intialNode.hash(), true);
            int openedNodesCount = 0;
            while (!frontier.isEmpty()) {
                openedNodesCount++;
                Node temp = frontier.pop();
                inFrontier.remove(temp.hash());
                explored.put(temp.hash(), true);
                if (temp.f <= cutoff) {
                    ArrayList<Node> children = temp.successor();
                    for (int i = 0; i < children.size(); i++) {
                        if (!(inFrontier.containsKey(children.get(i).hash())) && !(explored.containsKey(children.get(i).hash()))) {
                            if (isGoal(children.get(i))) {
                                result(children.get(i));
                                System.out.println("opened nodes number= " + openedNodesCount);
                                System.out.println("g=" + children.get(i).g);
                                return;
                            }
                            children.get(i).f = this.f(children.get(i), keyNodeI, keyNodeJ, castleNodeI, castleNodeJ, children.get(i).level);
                            children.get(i).g = children.get(i).g + 1;
                            frontier.push(children.get(i));
                            inFrontier.put(children.get(i).hash(), true);
                        }
                    }
                } else {
                    bigFrontier.add(temp);
                }
            }
            cutoff = peekTheBestNode(bigFrontier).f;
        }
    }

    public Node peekTheBestNode(Queue<Node> frontier) {
        Node[] array = new Node[frontier.size()];
        int min = 999999999;
        int minIndex = -1;
        for (int i = 0; i < array.length; i++) {
            array[i] = frontier.poll().copy();
        }
        for (int i = 0; i < array.length; i++) {
            Node node = array[i];
            if (array[i].f < min) {
                min = array[i].f;
                minIndex = i;
            }
        }
        for (int i = 0; i < array.length; i++) {
            if (i != minIndex) {
                frontier.add(array[i]);
            }

        }
        return array[minIndex];

    }

    public int h(Node currentNode, int keyNodeI, int keyNodeJ, int castleNodeI, int castleNodeJ) {
        if (currentNode.player.haskey) {
            int x = castleNodeI - currentNode.player.i;
            if (x < 0) {
                x = -1 * x;
            }
            int y = castleNodeJ - currentNode.player.j;
            if (y < 0) {
                y = -1 * y;
            }
            int distance = x + y;
            return distance;
        } else {
            int x = keyNodeI - currentNode.player.i;
            if (x < 0) {
                x = -1 * x;
            }
            int y = keyNodeJ - currentNode.player.j;
            if (y < 0) {
                y = -1 * y;
            }
            int distanceFromKey = x + y;
            int x2 = castleNodeI - keyNodeI;
            if (x2 < 0) {
                x2 = -1 * x2;
            }
            int y2 = castleNodeJ - keyNodeJ;
            if (y2 < 0) {
                y2 = -1 * y2;
            }
            int distance = x2 + y2;
            return distanceFromKey + distance;

        }
    }

    public int f(Node currentNode, int keyNodeI, int keyNodeJ, int castleNodeI, int castleNodeJ, int g) {
        return this.h(currentNode, keyNodeI, keyNodeJ, castleNodeI, castleNodeJ) + g;
    }

    public boolean isGoal(Node node) {
        if (node.map.at(node.player.i, node.player.j).name == 'C') {
            return true;
        } else {
            return false;
        }
    }

    public void result(Node node) {
        Stack<Node> nodes = new Stack<Node>();
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
            FileWriter myWriter = new FileWriter("result_IDA_star.txt");
            while (!nodes.empty()) {
                Node tempNode = nodes.pop();
                String action = tempNode.priviousAction;
                System.out.println(action + " " + tempNode.player.money + " " + tempNode.player.food);
                myWriter.write(action + "\n");
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
