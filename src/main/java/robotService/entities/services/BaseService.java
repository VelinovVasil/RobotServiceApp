package robotService.entities.services;

import robotService.common.ConstantMessages;
import robotService.common.ExceptionMessages;
import robotService.entities.robot.Robot;
import robotService.entities.supplements.Supplement;

import java.util.ArrayList;
import java.util.Collection;

public abstract class BaseService implements Service {

    private String name;
    private int capacity;
    private Collection<Supplement> supplements;
    private Collection<Robot> robots;

    protected BaseService(String name, int capacity) {
        this.setName(name);
        this.capacity = capacity;
        this.supplements = new ArrayList<>(); // Collection may vary
        this.robots = new ArrayList<>(); // Collection may vary
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public void setName(String name) {
        if (name == null || name.trim().equals("")) {
            throw new NullPointerException(ExceptionMessages.SERVICE_NAME_CANNOT_BE_NULL_OR_EMPTY);
        }

        this.name = name;
    }

    @Override
    public Collection<Robot> getRobots() {
        return this.robots;
    }

    @Override
    public Collection<Supplement> getSupplements() {
        return this.supplements;
    }

    @Override
    public void addRobot(Robot robot) {
        if (this.capacity == this.robots.size()) {
            throw new IllegalStateException(ConstantMessages.NOT_ENOUGH_CAPACITY_FOR_ROBOT);
        }

        this.robots.add(robot);

    }

    @Override
    public void removeRobot(Robot robot) {
        this.robots.remove(robot);
    }

    @Override
    public void addSupplement(Supplement supplement) {
        this.supplements.add(supplement);
    }

    @Override
    public void feeding() {

        for (Robot robot: this.robots) {
            robot.eating();
        }

    }

    @Override
    public int sumHardness() {

        int sum = 0;

        for (Supplement supplement: this.getSupplements()) {
            sum += supplement.getHardness();
        }

        return sum;
    }

    @Override
    public String getStatistics() {

        StringBuilder sb = new StringBuilder();

        sb.append(String.format("%s %s:", this.getName(), this.getClass().getSimpleName()));
        sb.append(System.lineSeparator());

        if (this.robots.size() == 0) {

            sb.append("Robots: none");

        } else {

            sb.append("Robots:");

            for (Robot robot: this.robots) {

                sb.append(" " + robot.getName());

            }

        }

        sb.append(System.lineSeparator());

//        Supplements: {supplementsCount} Hardness: {sumHardness}"

        sb.append(String.format("Supplements: %s Hardness: %s", this.supplements.size(), this.sumHardness()));

        return sb.toString();
    }
}
