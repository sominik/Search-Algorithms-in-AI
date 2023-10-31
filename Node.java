
import java.util.ArrayList;

public class Node {

    public Map map;
    public Player player;
    public Node parentNode;
    public String priviousAction;
    public int level;
    public int f=0;
    public int g;

    public Node(Player player, Map map, Node parentNode, String priviousAction) {
        this.map = map.copy();
        this.player = new Player(player.i, player.j, player.money, player.food, player.haskey);
        this.parentNode = parentNode;
        this.priviousAction = priviousAction;
        if (parentNode == null) {
            this.level = 0;
        } else {
            this.level = parentNode.level + 1;
        }
        this.g=this.level;
    }
    
    public void resetG(){
        this.g=this.level;
    }
    
    public Node copy(){
        Node copyNode=new Node(this.player,this.map,this.parentNode,this.priviousAction);
        copyNode.map=this.map.copy();
        copyNode.f=this.f;
        copyNode.player=new Player(this.player.i, this.player.j, this.player.money, this.player.food, this.player.haskey);
        if (copyNode.parentNode == null) {
            copyNode.level = 0;
        } else {
            copyNode.level = parentNode.level + 1;
        }
        return copyNode;
    }

    public String hash() {
        int key = player.haskey ? 1 : 0;
        String result = player.i + "," + player.j + "," + player.money + "," + player.food + "," + key;
        int size = map.game.size();
        for (int i = 0; i < size; i++) {
            if (map.game.get(i) instanceof Bridge) {
                key = ((Bridge) map.game.get(i)).traveresd ? 1 : 0;
                result += key;
            } else if (map.game.get(i) instanceof Loot) {
                key = ((Loot) map.game.get(i)).used ? 1 : 0;
                result += key;
            }
        }
        return result;
    }

    public ArrayList<Node> successor() {
        ArrayList<Node> result = new ArrayList<Node>();
        if (this.player.j < this.map.cols - 1) {//player can move right
            BaseEntity entity = this.map.at(this.player.i, this.player.j + 1);
            if (entity.name != 'S') {
                if (entity.name == 'G') {
                    Node temp = new Node(this.player, this.map, this, "right");
                    temp.player.j++;
                    result.add(temp);
                } else if (entity.name == 'P') {
                    if (!((Bridge) entity).traveresd) {
                        Node temp = new Node(this.player, this.map, this, "right");
                        temp.player.j++;
                        ((Bridge) temp.map.at(temp.player.i, temp.player.j)).traveresd = true;
                        result.add(temp);
                    }
                } else if (entity.name == 'C') {
                    if (player.haskey) {
                        Node temp = new Node(this.player, this.map, this, "right");
                        temp.player.j++;
                        result.add(temp);
                    }
                } else if (entity.name == 'K') {
                    Node temp = new Node(this.player, this.map, this, "right");
                    temp.player.j++;
                    temp.player.haskey = true;
                    result.add(temp);
                } else if (entity.name == 'B') {
                    Bandit bandit = (Bandit) entity;
                    if (this.player.money > bandit.power) {
                        Node temp = new Node(this.player, this.map, this, "right");
                        temp.player.j++;
                        bandit.takeMoney(temp.player);
                        result.add(temp);
                    }
                } else if (entity.name == 'W') {
                    WildAnimall wildAnimall = (WildAnimall) entity;
                    if (this.player.food > wildAnimall.power) {
                        Node temp = new Node(this.player, this.map, this, "right");
                        temp.player.j++;
                        wildAnimall.takeFood(temp.player);
                        result.add(temp);
                    }
                } else if (entity.name == 'L') {
                    Loot loot = (Loot) entity;
                    if (loot.used) {
                        Node temp = new Node(this.player, this.map, this, "right");
                        temp.player.j++;
                        result.add(temp);
                    } else {
                        Node temp1 = new Node(this.player, this.map, this, "right, use money");
                        temp1.player.j++;
                        ((Loot) temp1.map.at(temp1.player.i, temp1.player.j)).useMoney(temp1.player);
                        result.add(temp1);
                        Node temp2 = new Node(this.player, this.map, this, "right, use food");
                        temp2.player.j++;
                        ((Loot) temp2.map.at(temp2.player.i, temp2.player.j)).useFood(temp2.player);
                        result.add(temp2);
                    }
                }
            }
        }
        if (this.player.j > 0) {//player can move left
            BaseEntity entity = this.map.at(this.player.i, this.player.j - 1);
            if (entity.name != 'S') {
                if (entity.name == 'G') {
                    Node temp = new Node(this.player, this.map, this, "left");
                    temp.player.j--;
                    result.add(temp);
                } else if (entity.name == 'P') {
                    if (!((Bridge) entity).traveresd) {
                        Node temp = new Node(this.player, this.map, this, "left");
                        temp.player.j--;
                        ((Bridge) temp.map.at(temp.player.i, temp.player.j)).traveresd = true;
                        result.add(temp);
                    }
                } else if (entity.name == 'C') {
                    if (player.haskey) {
                        Node temp = new Node(this.player, this.map, this, "left");
                        temp.player.j--;
                        result.add(temp);
                    }
                } else if (entity.name == 'K') {
                    Node temp = new Node(this.player, this.map, this, "left");
                    temp.player.j--;
                    temp.player.haskey = true;
                    result.add(temp);
                } else if (entity.name == 'B') {
                    Bandit bandit = (Bandit) entity;
                    if (this.player.money > bandit.power) {
                        Node temp = new Node(this.player, this.map, this, "left");
                        temp.player.j--;
                        bandit.takeMoney(temp.player);
                        result.add(temp);
                    }
                } else if (entity.name == 'W') {
                    WildAnimall wildAnimall = (WildAnimall) entity;
                    if (this.player.food > wildAnimall.power) {
                        Node temp = new Node(this.player, this.map, this, "left");
                        temp.player.j--;
                        wildAnimall.takeFood(temp.player);
                        result.add(temp);
                    }
                } else if (entity.name == 'L') {
                    Loot loot = (Loot) entity;
                    if (loot.used) {
                        Node temp = new Node(this.player, this.map, this, "left");
                        temp.player.j--;
                        result.add(temp);
                    } else {
                        Node temp1 = new Node(this.player, this.map, this, "left, use money");
                        temp1.player.j--;
                        ((Loot) temp1.map.at(temp1.player.i, temp1.player.j)).useMoney(temp1.player);
                        result.add(temp1);
                        Node temp2 = new Node(this.player, this.map, this, "left, use food");
                        temp2.player.j--;
                        ((Loot) temp2.map.at(temp2.player.i, temp2.player.j)).useFood(temp2.player);
                        result.add(temp2);
                    }
                }
            }
        }
        if (this.player.i > 0) {//player can move up
            BaseEntity entity = this.map.at(this.player.i - 1, this.player.j);
            if (entity.name != 'S') {
                if (entity.name == 'G') {
                    Node temp = new Node(this.player, this.map, this, "up");
                    temp.player.i--;
                    result.add(temp);
                } else if (entity.name == 'P') {
                    if (!((Bridge) entity).traveresd) {
                        Node temp = new Node(this.player, this.map, this, "up");
                        temp.player.i--;
                        ((Bridge) temp.map.at(temp.player.i, temp.player.j)).traveresd = true;
                        result.add(temp);
                    }
                } else if (entity.name == 'C') {
                    if (player.haskey) {
                        Node temp = new Node(this.player, this.map, this, "up");
                        temp.player.i--;
                        result.add(temp);
                    }
                } else if (entity.name == 'K') {
                    Node temp = new Node(this.player, this.map, this, "up");
                    temp.player.i--;
                    temp.player.haskey = true;
                    result.add(temp);
                } else if (entity.name == 'B') {
                    Bandit bandit = (Bandit) entity;
                    if (this.player.money > bandit.power) {
                        Node temp = new Node(this.player, this.map, this, "up");
                        temp.player.i--;
                        bandit.takeMoney(temp.player);
                        result.add(temp);
                    }
                } else if (entity.name == 'W') {
                    WildAnimall wildAnimall = (WildAnimall) entity;
                    if (this.player.food > wildAnimall.power) {
                        Node temp = new Node(this.player, this.map, this, "up");
                        temp.player.i--;
                        wildAnimall.takeFood(temp.player);
                        result.add(temp);
                    }
                } else if (entity.name == 'L') {
                    Loot loot = (Loot) entity;
                    if (loot.used) {
                        Node temp = new Node(this.player, this.map, this, "up");
                        temp.player.i--;
                        result.add(temp);
                    } else {
                        Node temp1 = new Node(this.player, this.map, this, "up, use money");
                        temp1.player.i--;
                        ((Loot) temp1.map.at(temp1.player.i, temp1.player.j)).useMoney(temp1.player);
                        result.add(temp1);
                        Node temp2 = new Node(this.player, this.map, this, "up, use food");
                        temp2.player.i--;
                        ((Loot) temp2.map.at(temp2.player.i, temp2.player.j)).useFood(temp2.player);
                        result.add(temp2);
                    }
                }
            }
        }
        if (this.player.i < this.map.rows - 1) {//player can move down
            BaseEntity entity = this.map.at(this.player.i + 1, this.player.j);
            if (entity.name != 'S') {
                if (entity.name == 'G') {
                    Node temp = new Node(this.player, this.map, this, "down");
                    temp.player.i++;
                    result.add(temp);
                } else if (entity.name == 'P') {
                    if (!((Bridge) entity).traveresd) {
                        Node temp = new Node(this.player, this.map, this, "down");
                        temp.player.i++;
                        ((Bridge) temp.map.at(temp.player.i, temp.player.j)).traveresd = true;
                        result.add(temp);
                    }
                } else if (entity.name == 'C') {
                    if (player.haskey) {
                        Node temp = new Node(this.player, this.map, this, "down");
                        temp.player.i++;
                        result.add(temp);
                    }
                } else if (entity.name == 'K') {
                    Node temp = new Node(this.player, this.map, this, "down");
                    temp.player.i++;
                    temp.player.haskey = true;
                    result.add(temp);
                } else if (entity.name == 'B') {
                    Bandit bandit = (Bandit) entity;
                    if (this.player.money > bandit.power) {
                        Node temp = new Node(this.player, this.map, this, "down");
                        temp.player.i++;
                        bandit.takeMoney(temp.player);
                        result.add(temp);
                    }
                } else if (entity.name == 'W') {
                    WildAnimall wildAnimall = (WildAnimall) entity;
                    if (this.player.food > wildAnimall.power) {
                        Node temp = new Node(this.player, this.map, this, "down");
                        temp.player.i++;
                        wildAnimall.takeFood(temp.player);
                        result.add(temp);
                    }
                } else if (entity.name == 'L') {
                    Loot loot = (Loot) entity;
                    if (loot.used) {
                        Node temp = new Node(this.player, this.map, this, "down");
                        temp.player.i++;
                        result.add(temp);
                    } else {
                        Node temp1 = new Node(this.player, this.map, this, "down, use money");
                        temp1.player.i++;
                        ((Loot) temp1.map.at(temp1.player.i, temp1.player.j)).useMoney(temp1.player);
                        result.add(temp1);
                        Node temp2 = new Node(this.player, this.map, this, "down, use food");
                        temp2.player.i++;
                        ((Loot) temp2.map.at(temp2.player.i, temp2.player.j)).useFood(temp2.player);
                        result.add(temp2);
                    }
                }
            }
        }
        return result;
    }

    public ArrayList<Node> reversed_successor() {//used for BDS
        ArrayList<Node> result = new ArrayList<Node>();
        if (this.player.j < this.map.cols - 1) {//player can move right
            BaseEntity entity = this.map.at(this.player.i, this.player.j + 1);
            if (entity.name != 'S' || entity.name != 'C') {
                if (entity.name == 'G') {
                    Node temp = new Node(this.player, this.map, this, "right");
                    temp.player.j++;
                    result.add(temp);
                } else if (entity.name == 'P') {
                    if (((Bridge) entity).traveresd) {
                        Node temp = new Node(this.player, this.map, this, "right");
                        temp.player.j++;
                        ((Bridge) temp.map.at(temp.player.i, temp.player.j)).traveresd = false;
                        result.add(temp);
                    }
                } else if (entity.name == 'K') {
                    Node temp = new Node(this.player, this.map, this, "right");
                    temp.player.j++;
                    temp.player.haskey = false;
                    result.add(temp);
                } else if (entity.name == 'B') {
                    Bandit bandit = (Bandit) entity;
                    Node temp = new Node(this.player, this.map, this, "right");
                    temp.player.j++;
                    player.changeMoney(bandit.power);
                    result.add(temp);
                } else if (entity.name == 'W') {
                    WildAnimall wildAnimall = (WildAnimall) entity;
                    Node temp = new Node(this.player, this.map, this, "right");
                    temp.player.j++;
                    player.changeFood(wildAnimall.power);
                    result.add(temp);
                } else if (entity.name == 'L') {
                    Loot loot = (Loot) entity;
                    if (loot.used) {
                        loot.used = false;
                        if (loot.useMoney) {
                            Node temp1 = new Node(this.player, this.map, this, "right, use money");
                            player.changeMoney(-1 * loot.money);
                            temp1.player.j++;
                            result.add(temp1);
                            loot.useMoney = false;
                        } else if (loot.useFood) {
                            Node temp2 = new Node(this.player, this.map, this, "right, use food");
                            player.changeFood(-1 * player.food);
                            temp2.player.j++;
                            result.add(temp2);
                            loot.useFood = false;
                        }
                    }
                }
            }
        }
        if (this.player.j > 0) {//player can move left
            BaseEntity entity = this.map.at(this.player.i, this.player.j - 1);
            if (entity.name != 'S' || entity.name != 'C') {
                if (entity.name == 'G') {
                    Node temp = new Node(this.player, this.map, this, "left");
                    temp.player.j--;
                    result.add(temp);
                } else if (entity.name == 'P') {
                    if (((Bridge) entity).traveresd) {
                        Node temp = new Node(this.player, this.map, this, "left");
                        temp.player.j--;
                        ((Bridge) temp.map.at(temp.player.i, temp.player.j)).traveresd = false;
                        result.add(temp);
                    }
                } else if (entity.name == 'K') {
                    Node temp = new Node(this.player, this.map, this, "left");
                    temp.player.j--;
                    temp.player.haskey = false;
                    result.add(temp);
                } else if (entity.name == 'B') {
                    Bandit bandit = (Bandit) entity;
                    Node temp = new Node(this.player, this.map, this, "left");
                    temp.player.j--;
                    player.changeMoney(bandit.power);
                    result.add(temp);
                } else if (entity.name == 'W') {
                    WildAnimall wildAnimall = (WildAnimall) entity;
                    Node temp = new Node(this.player, this.map, this, "left");
                    temp.player.j--;
                    player.changeFood(wildAnimall.power);
                    result.add(temp);
                } else if (entity.name == 'L') {
                    Loot loot = (Loot) entity;
                    if (loot.used) {
                        loot.used = false;
                        if (loot.useMoney) {
                            Node temp1 = new Node(this.player, this.map, this, "left, use money");
                            player.changeMoney(-1 * loot.money);
                            temp1.player.j--;
                            result.add(temp1);
                            loot.useMoney = false;
                        } else if (loot.useFood) {
                            Node temp2 = new Node(this.player, this.map, this, "left, use food");
                            player.changeFood(-1 * player.food);
                            temp2.player.j--;
                            result.add(temp2);
                            loot.useFood = false;
                        }
                    }
                }
            }
        }
        if (this.player.i > 0) {//player can move up
            BaseEntity entity = this.map.at(this.player.i - 1, this.player.j);
            if (entity.name != 'S' || entity.name != 'C') {
                if (entity.name == 'G') {
                    Node temp = new Node(this.player, this.map, this, "up");
                    temp.player.i--;
                    result.add(temp);
                } else if (entity.name == 'P') {
                    if (!((Bridge) entity).traveresd) {
                        Node temp = new Node(this.player, this.map, this, "up");
                        temp.player.i--;
                        ((Bridge) temp.map.at(temp.player.i, temp.player.j)).traveresd = false;
                        result.add(temp);
                    }
                } else if (entity.name == 'K') {
                    Node temp = new Node(this.player, this.map, this, "up");
                    temp.player.i--;
                    temp.player.haskey = false;
                    result.add(temp);
                } else if (entity.name == 'B') {
                    Bandit bandit = (Bandit) entity;
                    Node temp = new Node(this.player, this.map, this, "up");
                    temp.player.i--;
                    player.changeMoney(bandit.power);
                    result.add(temp);
                } else if (entity.name == 'W') {
                    WildAnimall wildAnimall = (WildAnimall) entity;
                    Node temp = new Node(this.player, this.map, this, "up");
                    temp.player.i--;
                    player.changeFood(wildAnimall.power);
                    result.add(temp);
                } else if (entity.name == 'L') {
                    Loot loot = (Loot) entity;
                    if (loot.used) {
                        loot.used = false;
                        if (loot.useMoney) {
                            Node temp1 = new Node(this.player, this.map, this, "up, use money");
                            player.changeMoney(-1 * loot.money);
                            temp1.player.i--;
                            result.add(temp1);
                            loot.useMoney = false;
                        } else if (loot.useFood) {
                            Node temp2 = new Node(this.player, this.map, this, "up, use food");
                            player.changeFood(-1 * player.food);
                            temp2.player.i--;
                            result.add(temp2);
                            loot.useFood=false;
                        }
                    }
                }
            }
        }
        if (this.player.i < this.map.rows - 1) {//player can move down
            BaseEntity entity = this.map.at(this.player.i + 1, this.player.j);
            if (entity.name != 'S' || entity.name != 'C') {
                if (entity.name == 'G') {
                    Node temp = new Node(this.player, this.map, this, "down");
                    temp.player.i++;
                    result.add(temp);
                } else if (entity.name == 'P') {
                    if (!((Bridge) entity).traveresd) {
                        Node temp = new Node(this.player, this.map, this, "down");
                        temp.player.i++;
                        ((Bridge) temp.map.at(temp.player.i, temp.player.j)).traveresd = false;
                        result.add(temp);
                    }
                } else if (entity.name == 'K') {
                    Node temp = new Node(this.player, this.map, this, "down");
                    temp.player.i++;
                    temp.player.haskey = false;
                    result.add(temp);
                } else if (entity.name == 'B') {
                    Bandit bandit = (Bandit) entity;
                    Node temp = new Node(this.player, this.map, this, "down");
                    temp.player.i++;
                    player.changeMoney(bandit.power);
                    result.add(temp);
                } else if (entity.name == 'W') {
                    WildAnimall wildAnimall = (WildAnimall) entity;
                    Node temp = new Node(this.player, this.map, this, "down");
                    temp.player.i++;
                    player.changeFood(wildAnimall.power);
                    result.add(temp);
                } else if (entity.name == 'L') {
                    Loot loot = (Loot) entity;
                    if (loot.used) {
                        loot.used = false;
                        if (loot.useMoney) {
                            Node temp1 = new Node(this.player, this.map, this, "down, use money");
                            player.changeMoney(-1 * loot.money);
                            temp1.player.i++;
                            result.add(temp1);
                            loot.useMoney = false;
                        } else if (loot.useFood) {
                            Node temp2 = new Node(this.player, this.map, this, "down, use food");
                            player.changeFood(-1 * player.food);
                            temp2.player.i++;
                            result.add(temp2);
                            loot.useFood=false;
                        }
                    }
                }
            }
        }
        return result;
    }
}
