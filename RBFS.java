
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

public class RBFS {

    Visualizer visualizer = new Visualizer();

    public void search(Node intialNode) {

        Stack<Node> frontier = new Stack<Node>();
        Hashtable<String, Boolean> inFrontier = new Hashtable<>();
        Hashtable<String, Boolean> explored = new Hashtable<>();

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
        System.out.println("key(" + keyNodeI + "," + keyNodeJ + ")");
        System.out.println("castle(" + castleNodeI + "," + castleNodeJ + ")");
        if (isGoal(intialNode)) {
            result(intialNode);
            return;
        }
        intialNode.f = this.f(intialNode, keyNodeI, keyNodeJ, castleNodeI, castleNodeJ, intialNode.g);
        intialNode.g = intialNode.g + 1;
        frontier.add(intialNode);
        inFrontier.put(intialNode.hash(), true);
        int openedNodesCount = 0;
        int secondChoiceCost = 999999999;//a big integer
        while (!frontier.isEmpty()) {
            Node bestNode = peekTheBestNode(frontier);
            if (!frontier.isEmpty()) {
                Node secondChoice = peekTheBestNode(frontier);
                secondChoiceCost = secondChoice.f;
                frontier.add(secondChoice);
            }
            openedNodesCount++;
            inFrontier.remove(bestNode.hash());
            explored.put(bestNode.hash(), true);
            ArrayList<Node> children = bestNode.successor();
            int bigChildCounter = 0;
            for (int i = 0; i < children.size(); i++) {
                if (!(inFrontier.containsKey(children.get(i).hash())) && !(explored.containsKey(children.get(i).hash()))) {
                    if (isGoal(children.get(i))) {
                        result(children.get(i));
                        System.out.println("opened nodes number= " + openedNodesCount);
                        return;
                    }
                    children.get(i).f = this.f(children.get(i), keyNodeI, keyNodeJ, castleNodeI, castleNodeJ, children.get(i).level);
                    children.get(i).g = children.get(i).g + 1;
                    frontier.add(children.get(i));
                    inFrontier.put(children.get(i).hash(), true);
                }
                if (children.get(i).f > secondChoiceCost) {
                    bigChildCounter++;
                }
            }
            if (bigChildCounter == children.size()) {
                int bestChildF = this.peekTheBestChild(children).f;
                bestNode.f = bestChildF;
                for (int i = 0; i < children.size(); i++) {
                    frontier.pop();
                }
                frontier.push(bestNode);
            }
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

    public Node peekTheBestNode(Stack<Node> frontier) {
        Node[] array = new Node[frontier.size()];
        int min = 999999999;
        int minIndex = -1;
        for (int i = 0; i < array.length; i++) {
            array[i] = frontier.pop().copy();
        }
        for (int i = 0; i < array.length; i++) {
            Node node = array[i];
            if (array[i].f < min) {
                min = array[i].f;
                minIndex = i;
            }
        }
        for (int i = array.length - 1; i >= 0; i--) {
            if (i != minIndex) {
                frontier.push(array[i]);
            }

        }
        return array[minIndex];

    }

    public Node peekTheBestChild(ArrayList<Node> array) {
        int min = 999999999;
        int minIndex = -1;

        for (int i = 0; i < array.size(); i++) {
            if (array.get(i).f < min) {
                min = array.get(i).f;
                minIndex = i;
            }
        }

        return array.remove(minIndex);

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
            FileWriter myWriter = new FileWriter("result_RBFS.txt");
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
