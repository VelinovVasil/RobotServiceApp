package robotService.core;

import robotService.common.ConstantMessages;
import robotService.common.ExceptionMessages;
import robotService.entities.robot.FemaleRobot;
import robotService.entities.robot.MaleRobot;
import robotService.entities.robot.Robot;
import robotService.entities.services.MainService;
import robotService.entities.services.SecondaryService;
import robotService.entities.services.Service;
import robotService.entities.supplements.MetalArmor;
import robotService.entities.supplements.PlasticArmor;
import robotService.entities.supplements.Supplement;
import robotService.repositories.SupplementRepository;

import java.util.ArrayList;
import java.util.Collection;

public class ControllerImpl implements Controller {

    private SupplementRepository supplements;
    private Collection<Service> services;

    // A more efficient solution to this problem is to use a Map instead of a List in order to access a service by name

    public ControllerImpl() {
        this.supplements = new SupplementRepository();
        this.services = new ArrayList<>();
    }

    @Override
    public String addService(String type, String name) {

        if (type.equals("MainService")) {

            Service service = new MainService(name);
            this.services.add(service);

        } else if (type.equals("SecondaryService")) {

            Service service = new SecondaryService(name);
            this.services.add(service);

        } else {
            throw new NullPointerException(ExceptionMessages.INVALID_SERVICE_TYPE);
        }

        return String.format(ConstantMessages.SUCCESSFULLY_ADDED_SERVICE_TYPE, type);
    }

    @Override
    public String addSupplement(String type) {
        if (type.equals("PlasticArmor")) {

            Supplement supplement = new PlasticArmor();
            this.supplements.addSupplement(supplement);

        } else if (type.equals("MetalArmor")) {

            Supplement supplement = new MetalArmor();
            this.supplements.addSupplement(supplement);

        } else {
            throw new IllegalArgumentException(ExceptionMessages.INVALID_SUPPLEMENT_TYPE);
        }

        return String.format(ConstantMessages.SUCCESSFULLY_ADDED_SUPPLEMENT_TYPE, type);
    }

    @Override
    public String supplementForService(String serviceName, String supplementType) {

        if (this.supplements.findFirst(supplementType) == null) {
            throw new IllegalArgumentException(String.format(ExceptionMessages.NO_SUPPLEMENT_FOUND, supplementType));
        }

        for (Service service: this.services) {

            if (serviceName.equals(service.getName())) {

                service.addSupplement(this.supplements.findFirst(supplementType));
                this.supplements.removeSupplement(this.supplements.findFirst(supplementType));

            }

        }

        return String.format(ConstantMessages.SUCCESSFULLY_ADDED_SUPPLEMENT_IN_SERVICE, supplementType, serviceName);
    }

    @Override
    public String addRobot(String serviceName, String robotType, String robotName, String robotKind, double price) {
        if (robotType.equals("MaleRobot")) {
            Robot robot = new MaleRobot(robotName, robotKind, price);

            for (Service service: this.services) {

                if (service.getName().equals(serviceName)) {

                    if (service.getClass().getSimpleName().equals("MainService")) {

                        service.addRobot(robot);
                        return String.format(ConstantMessages.SUCCESSFULLY_ADDED_ROBOT_IN_SERVICE, robotType, serviceName);

                    } else {

                        return ConstantMessages.UNSUITABLE_SERVICE;

                    }

                }

            }

        } else if (robotType.equals("FemaleRobot")) {

            Robot robot = new FemaleRobot(robotName, robotKind, price);

            for (Service service: this.services) {

                if (service.getName().equals(serviceName)) {

                    if (service.getClass().getSimpleName().equals("SecondaryService")) {

                        service.addRobot(robot);
                        return String.format(ConstantMessages.SUCCESSFULLY_ADDED_ROBOT_IN_SERVICE, robotType, serviceName);

                    } else {

                        return ConstantMessages.UNSUITABLE_SERVICE;

                    }

                }

            }

        } else {

            throw new IllegalArgumentException(ExceptionMessages.INVALID_ROBOT_TYPE);

        }
        return null;
    }

    @Override
    public String feedingRobot(String serviceName) {

        int robotsFed = 0;

        for (Service service: this.services) {

            if (service.getName().equals(serviceName)) {

                service.feeding();
                robotsFed = service.getRobots().size();
            }

        }

        return String.format(ConstantMessages.FEEDING_ROBOT, robotsFed);
    }

    @Override
    public String sumOfAll(String serviceName) {

        double sum = 0;

        for (Service service: this.services) {

            if (service.getName().equals(serviceName)) {

                for (Robot robot: service.getRobots()) {
                    sum += robot.getPrice();
                }

                for (Supplement supplement: service.getSupplements()) {
                    sum += supplement.getPrice();
                }

            }

        }

        return String.format(ConstantMessages.VALUE_SERVICE, serviceName, sum);
    }

    @Override
    public String getStatistics() {
        StringBuilder sb = new StringBuilder();

        for (Service service: this.services) {

            sb.append(service.getStatistics());
            sb.append(System.lineSeparator());

        }

        sb.deleteCharAt(sb.length() - 1);

        return sb.toString();
    }
}
