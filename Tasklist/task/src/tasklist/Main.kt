package tasklist

import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import kotlin.system.exitProcess
import kotlinx.datetime.*
import kotlinx.datetime.TimeZone
import java.io.EOFException
import java.io.File
import java.io.FileNotFoundException
import java.lang.NumberFormatException
import java.util.*


fun inputPriority(): String {
    var priority: String
    println("Input the task priority (C, H, N, L):")
    priority = readln().uppercase(Locale.getDefault())
    while (priority != "C" && priority != "H" && priority != "N" && priority != "L") {
        println("Input the task priority (C, H, N, L):")
        priority = readln().uppercase(Locale.getDefault())
    }
    var priorityColor: String = ""
    if (priority == "C") {
        priorityColor = "\u001B[101m \u001B[0m"
    } else if (priority == "H") {
        priorityColor = "\u001B[103m \u001B[0m"
    } else if (priority == "N") {
        priorityColor = "\u001B[102m \u001B[0m"
    } else {
        priorityColor = "\u001B[104m \u001B[0m"
    }
        return priorityColor
}

fun inputDate(): LocalDateTime {
    println("Input the date (yyyy-mm-dd):")
    var date = readln().split("-")
    //val templateTime = "12:22"
    var tmp = LocalDateTime(2000, 1, 1, 12, 0)
    var isDateNotValid = true
    while (isDateNotValid) {
        try {
            //tmp = (date + "T" + templateTime).toLocalDateTime()
            tmp = LocalDateTime(date[0].toInt(), date[1].toInt(), date[2].toInt(), 12, 22)
            isDateNotValid = false
        } catch (e: RuntimeException) {
            println("The input date is invalid")
            println("Input the date (yyyy-mm-dd):")
            date = readln().split("-")
        }
    }
    return tmp
}

fun inputTime(): LocalDateTime {
    var tmp = LocalDateTime(2000, 1, 1, 12, 0)
    println("Input the time (hh:mm):")
    var time = readln().split(":")
    var isTimeNotValid = true
    while (isTimeNotValid) {
        try {
            //tmp = (date + "T" + time).toLocalDateTime()
            tmp = LocalDateTime(2000, 2, 2, time[0].toInt(), time[1].toInt())
            isTimeNotValid = false
        } catch (e: RuntimeException) {
            println("The input time is invalid")
            println("Input the time (hh:mm):")
            time = readln().split(":")
        }
    }
    return tmp
}

fun inputTask(): String {
    var readTask: String
    var task = ""
    var taskChunked: Boolean = false
    println("Input a new task (enter a blank line to end):")
    readTask = readln().trim()
    if (readTask == "") {
        println("The task is blank")
    } else if (readTask.length > 44) {
        val list = readTask.chunked(44)
        task = "${list[0]}|\n"
        for (index in 1 until list.size) {
            task += "|    |            |       |   |   |${list[index].padEnd(44, ' ')}|\n"
        }
        taskChunked = true
    } else if (!taskChunked) {
        task += "${readTask.padEnd(44, ' ')}|\n"
    }
    while (readTask != "") {
        //readTask = readTask.trim()
        readTask = readln().trim()
        if (readTask.length > 44) {
            val list = readTask.chunked(44)
            for (element in list) {
                task += "|    |            |       |   |   |${element.padEnd(44, ' ')}|\n"
            }
            taskChunked = true
        }
        if (readTask != "" && !taskChunked) {
            task += "|    |            |       |   |   |${readTask.padEnd(44, ' ')}|\n"
        }
    }
    return task
}

fun checkDueTag(date: LocalDate): String {
    val currentDate = Clock.System.now().toLocalDateTime(TimeZone.of("UTC+0")).date
    return if (currentDate.daysUntil(date) > 0) {
        "\u001B[102m \u001B[0m"
    } else if (currentDate.daysUntil(date) < 0) {
        "\u001B[101m \u001B[0m"
    } else {
        "\u001B[103m \u001B[0m"
    }
}

fun addTask(taskList: MutableList<MutableList<String>>) {
    val taskValue = mutableListOf<String>()
    var notBlankTask = true
    val priority: String = inputPriority()
    val date = inputDate()
    val time = inputTime()
    val task = inputTask()
    val dateForTag = LocalDate(date.year, date.month, date.dayOfMonth)
    val minutes: String = if (time.minute < 10) {
        "0${time.minute}"
    } else {
        time.minute.toString()
    }
    val hours: String = if (time.hour < 10) {
        "0${time.hour}"
    } else {
        time.hour.toString()
    }

    if (task == "") notBlankTask = false
    if (notBlankTask) {
        taskValue.add("${date.date}")
        taskValue.add("${hours}:${minutes}")
        taskValue.add(priority)
        taskValue.add(checkDueTag(dateForTag))
        taskValue.add(task)
        taskList.add(taskValue)
    }
}


fun printTask(taskList: MutableList<MutableList<String>>) {
    if (taskList.isEmpty()) {
        println("No tasks have been input")
    } else {
        print(
            "+----+------------+-------+---+---+--------------------------------------------+\n" +
                    "| N  |    Date    | Time  | P | D |                   Task                     |\n"
        )
        for (index in taskList.indices) {
            print("+----+------------+-------+---+---+--------------------------------------------+\n")
            print("| ${(index + 1).toString().padEnd(3, ' ')}|")
            print(" ${taskList[index][0]} | ${taskList[index][1]} | ${taskList[index][2]} | ${taskList[index][3]} |${taskList[index][4]}")
        }
        println("+----+------------+-------+---+---+--------------------------------------------+")
    }
}

fun deleteTask(taskList: MutableList<MutableList<String>>) {
    if (taskList.isEmpty()) {
        println("No tasks have been input")
    } else {
        printTask(taskList)
        println("Input the task number (1-${taskList.size}):")
        var taskNumberNotValid = true
        var taskNumber: Int = 0

        while (taskNumberNotValid) {
            try {
                taskNumber = readln().toInt()
                taskList.removeAt(taskNumber - 1)
                taskNumberNotValid = false
                println("The task is deleted")
            } catch (e: IndexOutOfBoundsException) {
                println("Invalid task number")
                println("Input the task number (1-${taskList.size}):")
            } catch (e: NumberFormatException) {
                println("Invalid task number")
                println("Input the task number (1-${taskList.size}):")
            }
        }
    }
}

fun editTask(taskList: MutableList<MutableList<String>>) {
    if (taskList.isEmpty()) {
        println("No tasks have been input")
    } else {
        printTask(taskList)
        println("Input the task number (1-${taskList.size}):")
        var taskNumberNotValid = true
        var taskNumber: Int = 0

        while (taskNumberNotValid) {
            try {
                taskNumber = readln().toInt()
                taskList[taskNumber - 1].isEmpty()
                taskNumberNotValid = false
            } catch (e: IndexOutOfBoundsException) {
                println("Invalid task number")
                println("Input the task number (1-${taskList.size}):")
            } catch (e: NumberFormatException) {
                println("Invalid task number")
                println("Input the task number (1-${taskList.size}):")
            }
        }
        println("Input a field to edit (priority, date, time, task):")
        var choice: String = readln()
        while (choice != "priority" && choice != "date" && choice != "time" && choice != "task") {
            println("Invalid field")
            println("Input a field to edit (priority, date, time, task):")
            choice = readln()
        }
        when (choice) {
            "priority" -> {
                taskList[taskNumber - 1][2] = inputPriority()
                println("The task is changed")
            }
            "date" -> {
                val date = inputDate()
                taskList[taskNumber - 1][0] = date.date.toString()
                taskList[taskNumber - 1][3] = checkDueTag(LocalDate(date.year, date.month, date.dayOfMonth))
                println("The task is changed")
            }
            "time" -> {
                val dateTime = inputTime()
                val minutes: String = if (dateTime.minute < 10) {
                    "0${dateTime.minute}"
                } else {
                    dateTime.minute.toString()
                }
                val hours: String = if (dateTime.hour < 10) {
                    "0${dateTime.hour}"
                } else {
                    dateTime.hour.toString()
                }
                taskList[taskNumber - 1][1] = "${hours}:${minutes}"
                println("The task is changed")
            }
            "task" -> {
                taskList[taskNumber - 1][4] = inputTask()
                println("The task is changed")
            }
        }
    }
}


fun loadJSON(): List<*>? {
    val moshi = Moshi.Builder()
        .add(KotlinJsonAdapterFactory())
        .build()
    val jsonFile: String
    try {
        jsonFile = File("tasklist.json").readText(Charsets.UTF_8)
        val taskAdapter = moshi.adapter(List::class.java)
        return taskAdapter.fromJson(jsonFile)
    } catch (e: EOFException) {}
    catch (e: FileNotFoundException) {}
    return mutableListOf<MutableList<String>>()
}

fun saveJSON(taskList: MutableList<MutableList<String>>, JSONFile: File) {
    val moshi = Moshi.Builder()
        .add(KotlinJsonAdapterFactory())
        .build()
    val taskAdapter = moshi.adapter(List::class.java)
    JSONFile.writeText(taskAdapter.toJson(taskList))
}

fun taskList() {
    val jsonFile = File("tasklist.json")
    val list = loadJSON()
    val taskList: MutableList<MutableList<String>> = list as MutableList<MutableList<String>>
    println("Input an action (add, print, edit, delete, end):")
    var choice: String = readln().ifBlank { null } ?: "default string value"
    while (choice != "") {
        when (choice) {
            "add" -> addTask(taskList)
            "print" -> printTask(taskList)
            "edit" -> editTask(taskList)
            "end" -> {
                saveJSON(taskList, jsonFile)
                println("Tasklist exiting!")
                exitProcess(0)
            }
            "delete" -> deleteTask(taskList)
            "default string value" -> println("The input action is invalid")
            else -> println("The input action is invalid")
        }
        print("Input an action (add, print, edit, delete, end):")
        print("\n")
        choice = readln().ifBlank { null } ?: "default string value"
    }
}


fun main() {
    taskList()
}


